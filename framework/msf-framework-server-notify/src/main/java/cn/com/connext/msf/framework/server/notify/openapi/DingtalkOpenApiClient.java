package cn.com.connext.msf.framework.server.notify.openapi;

import cn.com.connext.msf.framework.notify.model.DingtalkNotificationInfo;
import cn.com.connext.msf.framework.server.notify.utils.DingtalkHttpClient;
import cn.com.connext.msf.framework.server.notify.utils.DingtalkSecretEncode;
import cn.com.connext.msf.framework.utils.JSON;

import java.text.MessageFormat;

public class DingtalkOpenApiClient {

    public static void sendDingtalkMessage(DingtalkNotificationInfo dingtalkNotificationInfo) {
        String url = "https://oapi.dingtalk.com/robot/send?access_token={0}&sign={1}&timestamp={2}";
        Long timestamp = System.currentTimeMillis();
        String sign = DingtalkSecretEncode.encodeSecret(dingtalkNotificationInfo.getSecret(), timestamp);
        String json = DingtalkHttpClient.post(MessageFormat.format(url, dingtalkNotificationInfo.getToken(), sign, timestamp.toString()), JSON.toJsonString(dingtalkNotificationInfo));
    }
}
