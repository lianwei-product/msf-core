package cn.com.connext.msf.framework.transaciton.idempotent;

import cn.com.connext.msf.framework.transaciton.common.TransactionResult;

/**
 * 接口说明：幂等事务资源服务
 * 开发人员: 程瀚
 * 修订日期: 2019-11-13 17:10:48
 */
public interface IdempotentResourceService<Task> {

    /**
     * 是否已经执行
     */
    boolean applied(IdempotentTransaction transaction, Task task);

    /**
     * 执行事务任务
     */
    TransactionResult apply(IdempotentTransaction transaction, Task task);

    /**
     * 标记事务完成
     */
    void done(IdempotentTransaction transaction, Task task);

}
