package cn.com.connext.msf.framework.transaciton.idempotent;

/**
 * 接口说明：幂等事务状态
 * 开发人员: 程瀚
 * 修订日期: 2019-11-13 17:10:48
 */
public class IdempotentTransactionStatus {

    public final static String PENDING = "pending";
    public final static String APPLIED = "applied";
    public final static String DONE = "done";

}
