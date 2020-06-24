package cn.com.connext.msf.framework.transaciton.distribute;

import cn.com.connext.msf.framework.transaciton.common.TransactionResult;

public interface DistributeTransactionProcessor<Task> {


    /**
     * 投票阶段，不应对资源产生任何变更
     *
     * @return 返回false时，会中断事务执行，但不会触发cancel操作。
     */
    default TransactionResult vote(Task task) {
        return TransactionResult.SUCCESS;
    }

    /**
     * 执行阶段，需要幂等支持
     *
     * @return 返回false时，会中断事务执行，并触发cancel操作。
     */
    default TransactionResult commit(Task task) {
        return TransactionResult.SUCCESS;
    }

    /**
     * 取消阶段，需要幂等支持
     */
    default void cancel(Task task) {

    }

    /**
     * 处理器执行次序，数值越小，排序越前。
     *
     * @return 返回处理器执行次序
     */
    int priority();

}
