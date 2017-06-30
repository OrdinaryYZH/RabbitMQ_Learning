package com.genericyzh.rabbitmq.publishsubscribe;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 使用fanout类型的exchange转发消息
 *
 * @author genericyzh
 * @date 2017/6/30 11:46
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String message = getMessage(args);

        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();

    }

    private static String getMessage(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "info: Hello World!";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            stringBuilder.append(delimiter).append(strings[i]);
        }
        return stringBuilder.toString();
    }
}
