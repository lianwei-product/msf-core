package cn.com.connext.msf.framework.distributeid.exception;

/**
 * 业务异常 - 获取集群节点名称失败。
 */
public class NodeNameException extends RuntimeException {

    private final static String message = "Get cluster node name error.";

    public NodeNameException(Throwable cause) {
        super(message, cause, false, false);
    }
}
