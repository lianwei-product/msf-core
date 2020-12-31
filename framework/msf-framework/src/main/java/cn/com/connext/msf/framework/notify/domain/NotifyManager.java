package cn.com.connext.msf.framework.notify.domain;

import cn.com.connext.msf.framework.notify.event.SaasNotificationEvent;

public interface NotifyManager {

    void sendNotify(SaasNotificationEvent saasNotificationEvent);
}
