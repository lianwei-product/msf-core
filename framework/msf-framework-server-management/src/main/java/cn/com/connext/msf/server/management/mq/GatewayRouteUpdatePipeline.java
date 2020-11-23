package cn.com.connext.msf.server.management.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface GatewayRouteUpdatePipeline {
    String SEND = "platform_gateway_route_update_send";

    @Output(SEND)
    MessageChannel sendChannel();

}
