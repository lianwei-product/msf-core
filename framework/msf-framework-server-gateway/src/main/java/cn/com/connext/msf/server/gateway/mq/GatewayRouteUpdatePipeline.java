package cn.com.connext.msf.server.gateway.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GatewayRouteUpdatePipeline {
    String RECV = "platform_gateway_route_update_recv";

    @Input(RECV)
    MessageChannel recvChannel();

}
