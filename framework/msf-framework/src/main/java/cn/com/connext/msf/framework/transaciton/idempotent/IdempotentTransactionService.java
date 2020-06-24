package cn.com.connext.msf.framework.transaciton.idempotent;

/**
 * 接口说明：幂等事务服务
 * 开发人员: 程瀚
 * 修订日期: 2019-11-13 17:10:48
 */
public interface IdempotentTransactionService<Task> {

    /**
     * 初始化事务
     */
    IdempotentTransaction prepare(Task task);

    /**
     * 标记资源服务已执行完成
     */
    void apply(IdempotentTransaction transaction, Task task);

    /**
     * 标记事务服务已执行完成
     */
    void done(IdempotentTransaction transaction, Task task);

    /**
     * 删除事务（资源服务执行失败时）
     */
    void delete(IdempotentTransaction transaction, Task task);

}
