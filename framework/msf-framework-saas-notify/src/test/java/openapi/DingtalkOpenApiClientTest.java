package openapi;

import cn.com.connext.msf.framework.notify.model.DingtalkNotificationInfo;
import cn.com.connext.msf.framework.saas.notify.utils.DingtalkHttpClient;
import cn.com.connext.msf.framework.saas.notify.utils.DingtalkSecretEncode;
import cn.com.connext.msf.framework.utils.JSON;
import org.junit.Test;

import java.text.MessageFormat;

public class DingtalkOpenApiClientTest {

    @Test
    public void sendDingtalkMessage() {
        String[] phones=new String[]{"15105179343"};
        DingtalkNotificationInfo dingtalkNotificationInfo=DingtalkNotificationInfo.from("text","啊哈哈哈哈",phones,false,"111","222");
        String secret="SEC0c7e85710ba4a190578106a69b6ebfc82edca0f3142767ec1841866b032539b5";
        String token="189151f754a72687299985ee87e0b96c76d71db315559188c8382df4342cc6dc";
        String url = "https://oapi.dingtalk.com/robot/send?access_token={0}&sign={1}&timestamp={2}";
        Long timestamp = System.currentTimeMillis();
        String sign= DingtalkSecretEncode.encodeSecret(secret.toString(),timestamp);
        String json = DingtalkHttpClient.post(MessageFormat.format(url,token,sign,timestamp.toString()), JSON.toJsonString(dingtalkNotificationInfo));
        System.out.println(json);
    }
}
