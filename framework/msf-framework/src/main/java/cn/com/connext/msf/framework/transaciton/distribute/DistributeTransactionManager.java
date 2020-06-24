package cn.com.connext.msf.framework.transaciton.distribute;

import cn.com.connext.msf.framework.transaciton.common.TransactionResult;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对象说明：分布式事务管理器
 * 开发人员：程瀚
 * 摘要说明：
 * 修订日期：2019-11-14 09:43:46
 */
@SuppressWarnings("WeakerAccess")
public abstract class DistributeTransactionManager<Task, Processor extends DistributeTransactionProcessor<Task>> {

    private final List<Processor> processorChain;

    public DistributeTransactionManager(List<Processor> processorChain) {
        this.processorChain = processorChain;
        this.processorChain.sort(Comparator.comparingInt(Processor::priority));

        LoggerFactory.getLogger(getClass()).info("Init processors: {}", StringUtils.join(
                processorChain.stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toList()), ",")
        );
    }

    protected TransactionResult vote(Task task) {
        for (Processor processor : processorChain) {
            TransactionResult result = processor.vote(task);
            if (result.error()) {
                return result;
            }
        }
        return TransactionResult.SUCCESS;
    }

    public TransactionResult process(Task task) {
        List<Processor> cancelChain = Lists.newArrayList();
        TransactionResult result = commit(task, cancelChain);
        if (result.error()) {
            cancel(task, cancelChain);
        }
        return result;
    }

    protected TransactionResult commit(Task task, List<Processor> cancelChain) {
        for (Processor processor : processorChain) {
            cancelChain.add(processor);
            TransactionResult result = processor.commit(task);
            if (result.error()) {
                return result;
            }
        }
        return TransactionResult.SUCCESS;
    }

    protected void cancel(Task task, List<Processor> cancelChain) {
        for (Processor processor : cancelChain) {
            processor.cancel(task);
        }
    }


}
