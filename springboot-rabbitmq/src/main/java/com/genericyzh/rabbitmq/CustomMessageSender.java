package com.genericyzh.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomMessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    public CustomMessageSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 3000L)
    public void sendMessage() {
        final CustomMessage message = new CustomMessage("Hello there!", new Random().nextInt(50), false);
        LOGGER.info("Sending message...");
        rabbitTemplate.convertAndSend(MessagingApplication.EXCHANGE_NAME, MessagingApplication.ROUTING_KEY, message);
    }
}
