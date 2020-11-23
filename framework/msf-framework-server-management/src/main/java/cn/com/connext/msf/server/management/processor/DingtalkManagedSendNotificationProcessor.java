package cn.com.connext.msf.server.management.processor;

import cn.com.connext.msf.framework.notify.constant.DingtalkMessageType;
import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;
import cn.com.connext.msf.framework.notify.model.DingtalkNotificationInfo;
import cn.com.connext.msf.framework.notify.producer.SaasSendNotificationProducer;
import cn.com.connext.msf.framework.utils.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "managed.type", havingValue = "dingtalk", matchIfMissing = false)
public class DingtalkManagedSendNotificationProcessor implements ManagedSendNotificationProcessor {

    private String token;
    private String secret;
    private String phones;
    private boolean isAtAll;

    private SaasSendNotificationProducer saasSendNotificationProducer;

    public DingtalkManagedSendNotificationProcessor(@Value("${managed.dingtalk.phone}") String phones,
                                                    @Value("${managed.dingtalk.isAtAll}") boolean isAtAll,
                                                    @Value("${managed.dingtalk.token}") String token,
                                                    @Value("${managed.dingtalk.secret}") String secret,
                                                    SaasSendNotificationProducer saasSendNotificationProducer) {
        this.token = token;
        this.secret = secret;
        this.phones = phones;
        this.isAtAll = isAtAll;
        this.saasSendNotificationProducer = saasSendNotificationProducer;
    }

    @Override
    public void process(String content) {
        String[] phoneList = phones.split(",");
        DingtalkNotificationInfo dingtalkNotificationInfo = DingtalkNotificationInfo.from(DingtalkMessageType.TEXT, content, phoneList, isAtAll,token,secret);
        saasSendNotificationProducer.send(SaasNotificationEvent.from(SaasNotificationEvent.MessageType.DINGTALK, JSON.toJsonString(dingtalkNotificationInfo)));
    }
}
