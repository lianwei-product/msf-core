package cn.com.connext.msf.framework.partition;

import cn.com.connext.msf.framework.exception.BusinessException;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binder.ProducerProperties;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DynamicPartitionService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DynamicPartitionService.class);
    @Value("${spring.application.name}")
    private String serviceName;
    @Value("${server.port}")
    private int serverPort;
    private static final int MAX_RETRY_SIZE = 3;

    @Autowired
    BindingServiceProperties bindingServiceProperties;
    @Autowired
    DynamicPartitionInfoRepository dynamicPartitionInfoRepository;

    public DynamicPartitionInfo getPartitionInfo(BindingProperties properties) {
        DynamicPartitionInfo info = dynamicPartitionInfoRepository.findItem(properties.getDestination());
        if (info == null) {
            info = createMqPartitionInfo(properties);
        } else {
            info = updateMqPartitionInfo(properties, info);
        }
        return info;
    }

    private DynamicPartitionInfo createMqPartitionInfo(BindingProperties properties) {
        DynamicPartitionInfo result = new DynamicPartitionInfo();
        result.setId(properties.getDestination());
        result.setDestination(properties.getDestination());
        int concurrency = properties.getConsumer().getConcurrency();
        result.setPartitionCount(concurrency);
        for (int i = 0; i < concurrency; i++) {
            ConsumerInfo info = buildConsumerInfo(i);
            result.addConsumerInfo(info);
        }
        return result;
    }

    private DynamicPartitionInfo updateMqPartitionInfo(BindingProperties properties, DynamicPartitionInfo info) {
        List<ConsumerInfo> consumerList = info.getConsumers().stream()
                .sorted(Comparator.comparing(ConsumerInfo::getInstanceIndex))
                .collect(Collectors.toList());
        String host = getHost();
        int concurrency = properties.getConsumer().getConcurrency();
        for (ConsumerInfo consumerInfo : consumerList) {
            Instant targetTime = ZonedDateTime.now().plusSeconds(-60).toInstant();
            if (consumerInfo.getTimestamp().before(Date.from(targetTime)) || consumerInfo.getHost().equals(host)) {
                consumerInfo.setServiceName(serviceName);
                consumerInfo.setHost(host);
                consumerInfo.setTimestamp(new Date());
                concurrency--;
                if (concurrency <= 0) {
                    break;
                }
            }
        }
        for (int i = concurrency; i > 0; i--) {
            ConsumerInfo result = buildConsumerInfo(info.getConsumers().size());
            info.addConsumerInfo(result);
        }
        return info;
    }

    public void initPartition() {
        Set<BindingProperties> partitionConsumers = getPartitionConsumers();

        for (BindingProperties consumer : partitionConsumers) {
            if (consumer.getConsumer().getInstanceIndex() == -1) {
                if (!retry((n) -> initSingleChannel(consumer))) {
                    log.error("初始化队列分区[{}]乐观锁失败，重试超过{}次", consumer.getDestination(), MAX_RETRY_SIZE);
                    throw new BusinessException("初始化队列分区时乐观锁失败，重试超过重试次数");
                }
            }
        }
    }

    private boolean retry(Function<Void, Boolean> fn) {
        int retrySize = MAX_RETRY_SIZE;
        while (retrySize > 0) {
            if (fn.apply(null)) return true;
            retrySize--;
            if (retrySize <= 0) return false;
        }
        return true;
    }

    private boolean initSingleChannel(BindingProperties consumer) {
        try {
            DynamicPartitionInfo info = getPartitionInfo(consumer);
            List<Integer> instanceIndexList = info.getConsumers().stream()
                    .filter(it -> it.getHost().equals(getHost()))
                    .map(it -> it.getInstanceIndex())
                    .collect(Collectors.toList());

            dynamicPartitionInfoRepository.save(info);
            int partitionCount = info.getPartitionCount();
            updateProperties(consumer.getDestination(), partitionCount, instanceIndexList);
            return true;
        } catch (OptimisticLockingFailureException e) {
            log.warn("初始化队列分区[{}]乐观锁失败，重试初始化队列", consumer.getDestination());
            return false;
        }
    }

    public Set<BindingProperties> getPartitionProducers() {
        return bindingServiceProperties.getBindings()
                .values().stream().filter(it -> it.getProducer() != null && it.getProducer().isPartitioned())
                .collect(Collectors.toSet());
    }

    public Set<BindingProperties> getPartitionConsumers() {
        return bindingServiceProperties.getBindings()
                .values().stream().filter(it -> it.getConsumer() != null && it.getConsumer().isPartitioned())
                .collect(Collectors.toSet());
    }

    private String getKey(String destination, boolean producer) {
        return bindingServiceProperties.getBindings().entrySet().stream()
                .filter(it -> destination.equals(it.getValue().getDestination()))
                .filter(it -> (producer && it.getValue().getProducer() != null) ||
                        (!producer && it.getValue().getConsumer() != null))
                .map(it -> it.getKey()).findFirst().orElse(null);
    }

    private void updateProperties(String destination, int instanceCount, List<Integer> instanceIndexList) {
        String key = getKey(destination, false);
        getPartitionConsumers().stream()
                .filter(it -> it.getConsumer() != null && it.getDestination().equals(destination))
                .forEach(it -> {
                    BindingProperties oldProp = bindingServiceProperties.getBindings().remove(key);
                    oldProp.getConsumer().setConcurrency(1);
                    for (Integer index : instanceIndexList) {
                        BindingProperties newProp = copyBindingProperties(oldProp, index, instanceCount);
                        bindingServiceProperties.getBindings().put(key + "_" + index, newProp);
                    }
                });
        getPartitionProducers().stream()
                .filter(it -> it.getProducer() != null && it.getDestination().equals(destination))
                .forEach(it -> it.getProducer().setPartitionCount(instanceCount));

    }

    private BindingProperties copyBindingProperties(BindingProperties prop, Integer index, int instanceCount) {
        BindingProperties result = new BindingProperties();
        BeanUtils.copyProperties(prop, result);
        if (prop.getConsumer() != null) {
            result.setConsumer(new ConsumerProperties());
            BeanUtils.copyProperties(prop.getConsumer(), result.getConsumer());
        }
        if (prop.getProducer() != null) {
            result.setProducer(new ProducerProperties());
            BeanUtils.copyProperties(prop.getProducer(), result.getProducer());
        }
        result.getConsumer().setInstanceCount(instanceCount);
        result.getConsumer().setInstanceIndex(index);
        return result;
    }

    public void keepAlive() {
        updateConsumers();
        updateProducers();
    }

    private void updateConsumers() {
        Set<String> destinations = getPartitionConsumers().stream()
                .map(it -> it.getDestination())
                .collect(Collectors.toSet());
        for (String destination : destinations) {
            if (!retry((n) -> dynamicPartitionInfoRepository.updateTimestamp(destination, getHost(), new Date()))) {
                log.error("初始化队列分区[{}]乐观锁失败，重试超过{}次", destination, MAX_RETRY_SIZE);
                throw new BusinessException("初始化队列分区时乐观锁失败，重试超过重试次数");
            }
        }
    }

    private void updateProducers() {
        getPartitionProducers().stream().forEach(it -> {
            DynamicPartitionInfo info = dynamicPartitionInfoRepository.findItem(it.getDestination());
            if (info != null && it.getProducer().getPartitionCount() != info.getPartitionCount()) {
                log.info("MQ {} PartitionCount change from {} to {}", it.getDestination(),
                        it.getProducer().getPartitionCount(), info.getPartitionCount());
                it.getProducer().setPartitionCount(info.getPartitionCount());
            }
        });

    }

    private ConsumerInfo buildConsumerInfo(int instanceIndex) {
        ConsumerInfo info = new ConsumerInfo();
        info.setHost(getHost());
        info.setServiceName(serviceName);
        info.setInstanceIndex(instanceIndex);
        info.setTimestamp(new Date());
        return info;
    }

    public String getHost() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("找不到host", e);
        }
        return address.getHostAddress() + ":" + this.serverPort;
    }

}
