package cn.com.connext.msf.framework.partition;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.stream.binder.*;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.DataBinder;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 当前实现仅支持spring-cloud-stream 2.0.* 版本
 * 升级spring-cloud-stream后需要修改此类的实现
 */
@Service
public class ConnextBindingService extends BindingService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ConnextBindingService.class);
    private final CustomValidatorBean validator;
    private final Map<String, List<Binding<?>>> consumerBindings = new HashMap<>();

    public ConnextBindingService(BindingServiceProperties bindingServiceProperties, BinderFactory binderFactory) {
        this(bindingServiceProperties, binderFactory, null);
    }

    public ConnextBindingService(BindingServiceProperties bindingServiceProperties, BinderFactory binderFactory, TaskScheduler taskScheduler) {
        super(bindingServiceProperties, binderFactory, taskScheduler);
        this.validator = new CustomValidatorBean();
        this.validator.afterPropertiesSet();
    }

    private List<String> getPartitionInputName( String inputName){
        return getBindingServiceProperties().getBindings().keySet().stream()
            .filter(it -> it.startsWith(inputName))
            .collect(Collectors.toList());
    }

    @Override
    public <T> Collection<Binding<T>> bindConsumer(T input, String inputName) {
        List<String> inputNames = getPartitionInputName(inputName);
        Collection<Binding<T>> bindings = new ArrayList<>();
        for (String name : inputNames) {
            bindings.addAll(getBindings(input, name));
        }
        return bindings;
    }

    private <T> Collection<Binding<T>> getBindings(T input, String inputName) {
        String bindingTarget = this.getBindingServiceProperties()
            .getBindingDestination(inputName);
        String[] bindingTargets = StringUtils
            .commaDelimitedListToStringArray(bindingTarget);
        Collection<Binding<T>> bindings = new ArrayList<>();
        Binder<T, ConsumerProperties, ?> binder = (Binder<T, ConsumerProperties, ?>) getBinder(
            inputName, input.getClass());
        ConsumerProperties consumerProperties = this.getBindingServiceProperties()
            .getConsumerProperties(inputName);
        if (binder instanceof ExtendedPropertiesBinder) {
            Object extension = ((ExtendedPropertiesBinder) binder)
                .getExtendedConsumerProperties(inputName);
            ExtendedConsumerProperties extendedConsumerProperties = new ExtendedConsumerProperties(
                extension);
            BeanUtils.copyProperties(consumerProperties, extendedConsumerProperties);
            consumerProperties = extendedConsumerProperties;
        }
        validate(consumerProperties);
        for (String target : bindingTargets) {
            Binding<T> binding;
            if (input instanceof PollableSource) {
                binding = doBindPollableConsumer(input, inputName, binder, consumerProperties, target);
            }
            else {
                binding = doBindConsumer(input, inputName, binder, consumerProperties, target);
            }
            bindings.add(binding);
        }
        bindings = Collections.unmodifiableCollection(bindings);
        this.consumerBindings.put(inputName, new ArrayList<Binding<?>>(bindings));
        return bindings;
    }

    public void unbindConsumers(String inputName) {
        List<Binding<?>> bindings = this.consumerBindings.remove(inputName);
        if (bindings != null && !CollectionUtils.isEmpty(bindings)) {
            for (Binding<?> binding : bindings) {
                binding.unbind();
            }
        }
        else if (log.isWarnEnabled()) {
            log.warn("Trying to unbind '" + inputName + "', but no binding found.");
        }
    }

    private void validate(Object properties) {
        DataBinder dataBinder = new DataBinder(properties);
        dataBinder.setValidator(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            throw new IllegalStateException(dataBinder.getBindingResult().toString());
        }
    }
}
