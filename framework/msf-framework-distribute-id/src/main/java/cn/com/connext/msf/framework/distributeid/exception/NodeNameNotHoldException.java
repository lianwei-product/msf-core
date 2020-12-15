package cn.com.connext.msf.framework.distributeid.exception;

/**
 * 业务异常 - 尚未取得或未成功取得节点名称时，需抛出此异常。
 */
public class NodeNameNotHoldException extends RuntimeException {

    private final static String message = "Do not hold any cluster name.";

    public NodeNameNotHoldException() {
        super(message, null, false, false);
    }
}
