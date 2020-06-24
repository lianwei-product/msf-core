package cn.com.connext.msf.framework.transaciton.idempotent;

import cn.com.connext.msf.framework.transaciton.common.TransactionResult;

/**
 * 对象说明：幂等事务管理器
 * 开发人员：程瀚
 * 摘要说明：该对象用于简化接口幂等及本地事务的开发工作，可以不依赖于数据库事务，实现接口幂等调用及数据一致性自恢复。
 * 修订日期：2019-11-13 16:59:36
 *
 * @param <Task> 执行幂等事务所关联的任务信息
 */
public abstract class IdempotentTransactionManager<Task> {

    private final IdempotentResourceService<Task> resourceService;
    private final IdempotentTransactionService<Task> transactionService;


    /**
     * 实例化幂等事务管理器
     *
     * @param resourceService    资源服务实例
     * @param transactionService 事务服务实例
     */
    public IdempotentTransactionManager(IdempotentResourceService<Task> resourceService,
                                        IdempotentTransactionService<Task> transactionService) {
        this.resourceService = resourceService;
        this.transactionService = transactionService;
    }

    /**
     * 开始执行事务
     */
    public TransactionResult process(Task task) {
        TransactionResult result = TransactionResult.SUCCESS;
        IdempotentTransaction transaction = prepare(task);

        switch (transaction.getStatus()) {
            case IdempotentTransactionStatus.PENDING:
                result =apply(transaction, task);
                if (!result.error()) {
                    done(transaction, task);
                }
                break;

            case IdempotentTransactionStatus.APPLIED:
                done(transaction, task);
                break;

            case IdempotentTransactionStatus.DONE:
                // do nothing.
                break;
        }

        return result;
    }

    /**
     * 初始化事务
     */
    private IdempotentTransaction prepare(Task task) {
        return transactionService.prepare(task);
    }

    /**
     * 执行事务任务
     */
    private TransactionResult apply(IdempotentTransaction transaction, Task task) {
        if (!resourceService.applied(transaction, task)) {
            TransactionResult result =resourceService.apply(transaction, task);
            if (result.error()) {
                transactionService.delete(transaction, task);
                return result;
            }
        }
        transactionService.apply(transaction, task);
        return TransactionResult.SUCCESS;
    }

    /**
     * 标记事务完成
     */
    private void done(IdempotentTransaction transaction, Task task) {
        resourceService.done(transaction, task);
        transactionService.done(transaction, task);
    }

}
