package cn.com.connext.msf.framework.transaciton.idempotent;

import java.util.Date;

public class IdempotentDoneTransaction implements IdempotentTransaction {

    public static IdempotentDoneTransaction instance;

    static {
        instance = new IdempotentDoneTransaction();
        instance.status = IdempotentTransactionStatus.DONE;
    }

    private String status;

    public String getId() {
        return null;
    }

    public String getStatus() {
        return status;
    }

    public Date getUpdatedTime() {
        return new Date();
    }


}
