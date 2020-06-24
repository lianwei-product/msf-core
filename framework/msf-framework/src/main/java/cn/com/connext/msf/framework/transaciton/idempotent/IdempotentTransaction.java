package cn.com.connext.msf.framework.transaciton.idempotent;

import java.util.Date;

/**
 * 接口说明：幂等事务实体接口
 * 开发人员: 程瀚
 * 修订日期: 2019-11-13 17:10:48
 */
public interface IdempotentTransaction {


    /**
     * 唯一标识
     */
    String getId();

    /**
     * {@link IdempotentTransactionStatus}
     *
     * @return 返回当前事务状态
     */
    String getStatus();

    /**
     * 更新时间
     */
    Date getUpdatedTime();


}
