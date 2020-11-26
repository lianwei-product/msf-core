package cn.com.connext.msf.server.management.constant;

public class ManagementAuthority {

    private final static String APP = "management";

    private final static String CREATE = ".create";
    private final static String MODIFY = ".modify";
    private final static String DELETE = ".delete";
    private final static String LOCATE = ".locate";


    public final static String MANAGEMENT_SERVICE = APP + ".service";
    public final static String MANAGEMENT_SERVICE_CREATE = MANAGEMENT_SERVICE + CREATE;
    public final static String MANAGEMENT_SERVICE_MODIFY = MANAGEMENT_SERVICE + MODIFY;
    public final static String MANAGEMENT_SERVICE_DELETE = MANAGEMENT_SERVICE + DELETE;
    public final static String MANAGEMENT_SERVICE_LOCATE = MANAGEMENT_SERVICE + LOCATE;

    public final static String MANAGEMENT_ROUTE = APP + ".route";
    public final static String MANAGEMENT_ROUTE_CREATE = MANAGEMENT_ROUTE + CREATE;
    public final static String MANAGEMENT_ROUTE_MODIFY = MANAGEMENT_ROUTE + MODIFY;
    public final static String MANAGEMENT_ROUTE_DELETE = MANAGEMENT_ROUTE + DELETE;
    public final static String MANAGEMENT_ROUTE_LOCATE = MANAGEMENT_ROUTE + LOCATE;

    public final static String MANAGEMENT_ZONE = APP + ".zone";
    public final static String MANAGEMENT_ZONE_CREATE = MANAGEMENT_ZONE + CREATE;
    public final static String MANAGEMENT_ZONE_MODIFY = MANAGEMENT_ZONE + MODIFY;
    public final static String MANAGEMENT_ZONE_DELETE = MANAGEMENT_ZONE + DELETE;
    public final static String MANAGEMENT_ZONE_LOCATE = MANAGEMENT_ZONE + LOCATE;

    public final static String MANAGEMENT_SYSTEM_LOG = APP + ".system_log";
    public final static String MANAGEMENT_SYSTEM_LOG_LOCATE = MANAGEMENT_SYSTEM_LOG + LOCATE;

    public final static String MANAGEMENT_REQUEST_LOG = APP + ".request_log";
    public final static String MANAGEMENT_REQUEST_LOG_LOCATE = MANAGEMENT_REQUEST_LOG + LOCATE;
}
