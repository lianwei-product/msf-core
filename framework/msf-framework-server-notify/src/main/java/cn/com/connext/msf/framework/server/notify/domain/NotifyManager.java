package cn.com.connext.msf.framework.server.notify.domain;

import cn.com.connext.msf.framework.server.notify.event.SaasNotificationEvent;

public interface NotifyManager {

    void sendNotify(SaasNotificationEvent saasNotificationEvent);
}
