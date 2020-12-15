package cn.com.connext.msf.framework.server.notify.processor;

import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;

public interface NotificationSendEventProcessor {
    /**
     * @return 返回处理器执行次序，数值越小，排序越前。
     */
    default int order() {
        return 0;
    }


    boolean isMatch(SaasNotificationEvent.MessageType type);

    /**
     * 处理事件
     *
     * @param saasNotificationEvent 处理消息
     */
    void process(SaasNotificationEvent saasNotificationEvent);

}
