package cn.com.connext.msf.server.gateway.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GatewayAuthUpdatePipeline {
    String RECV = "platform_gateway_auth_update_recv";

    @Input(RECV)
    MessageChannel recvChannel();
}
