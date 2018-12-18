package com.genericyzh.rabbitmq;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMessageListener.class);

    @RabbitListener(queues = MessagingApplication.QUEUE_GENERIC_NAME)
    public void receiveMessage(final Message message, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        LOGGER.info("Received message as generic: {}", message.toString());
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(queues = MessagingApplication.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessage customMessage, @Headers Map<String, Object> headers, Channel channel) throws Exception {
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        LOGGER.info("Received message as specific class: {},DELIVERY_TAG: {}", customMessage.toString(), deliveryTag);
        channel.basicAck(deliveryTag, false);
    }
}
