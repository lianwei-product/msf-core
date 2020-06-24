package cn.com.connext.msf.framework.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class IpUtils {

    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isInRanges(List<String> ranges, String ip) {
        boolean flag = false;
        for (String ipRange : ranges) {
            flag = isInRange(ip, ipRange);
            if (flag) {
                return flag;
            }
        }
        return flag;
    }

    private static boolean isInRange(String ip, String cidr) {
        String[] ips = ip.split("\\.");


        int ipAddr = Integer.parseInt(ips[0]) << 24 | Integer.parseInt(ips[1]) << 16 | Integer.parseInt(ips[2]) << 8 | Integer.parseInt(ips[3]);
        int type = Integer.parseInt(cidr.replaceAll(".*/", ""));
        int mask = -1 << 32 - type;
        String cidrIp = cidr.replaceAll("/.*", "");
        String[] cidrIps = cidrIp.split("\\.");


        int cidrIpAddr = Integer.parseInt(cidrIps[0]) << 24 | Integer.parseInt(cidrIps[1]) << 16 | Integer.parseInt(cidrIps[2]) << 8 | Integer.parseInt(cidrIps[3]);

        return ((ipAddr & mask) == (cidrIpAddr & mask));
    }

}
