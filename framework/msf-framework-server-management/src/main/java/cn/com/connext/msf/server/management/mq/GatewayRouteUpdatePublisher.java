package cn.com.connext.msf.server.management.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@EnableBinding({GatewayRouteUpdatePipeline.class})
public class GatewayRouteUpdatePublisher {

    private final Logger logger = LoggerFactory.getLogger(GatewayRouteUpdatePublisher.class);
    private final MessageChannel sendChannel;

    @SuppressWarnings({"SpringJavaAutowiringInspection"})
    public GatewayRouteUpdatePublisher(@Qualifier(GatewayRouteUpdatePipeline.SEND) MessageChannel sendChannel) {
        this.sendChannel = sendChannel;
    }

    public void send(String routeId) {
        Message<String> message = MessageBuilder.withPayload(routeId).build();
        logger.info("Send route[" + routeId + "] update message to mq.");
        sendChannel.send(message);
    }
}
