package cn.com.connext.msf.framework.distributeid.exception;

/**
 * 业务异常 - 所有节点名称都在使用中时，需抛出此异常。
 */
public class NodeNameAllInUseException extends RuntimeException {

    private final static String message = "All cluster node names are in use.";

    public NodeNameAllInUseException() {
        super(message, null, false, false);
    }
}
