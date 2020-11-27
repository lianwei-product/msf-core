package cn.com.connext.msf.framework.saas.notify.processor;

import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.notify.model.DingtalkNotificationInfo;
import cn.com.connext.msf.framework.saas.notify.openapi.DingtalkOpenApiClient;
import cn.com.connext.msf.framework.utils.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DingtalkSendNotificationProcessor implements NotificationSendEventProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DingtalkSendNotificationProcessor.class);

    @Override
    public boolean isMatch(SaasNotificationEvent.MessageType type) {
        return (SaasNotificationEvent.MessageType.DINGTALK == type || SaasNotificationEvent.MessageType.EXIST == type);
    }

    @Override
    public void process(SaasNotificationEvent saasNotificationEvent) {
        String content = saasNotificationEvent.getContent();
        DingtalkNotificationInfo dingtalkNotificationInfo = null;
        try {
            dingtalkNotificationInfo = JSON.parseObject(content, DingtalkNotificationInfo.class);
        } catch (Exception e) {
            logger.error("DingtalkNotificationInfo 转换失败,content：" + content);
        }
        if (dingtalkNotificationInfo == null) return;
        DingtalkOpenApiClient.sendDingtalkMessage(dingtalkNotificationInfo);
    }
}
