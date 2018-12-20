package com.genericyzh.rabbitmq.book.cap1;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author genericyzh
 * @date 2018/10/25 23:17
 */
public class RabbitConsumer {

    public static final String QUEUE_NAME = "queue_demo";
    public static final String IP_ADDRESS = "192.168.0.111";
    public static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = {new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("root");
        factory.setPassword("root123");

        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        // 设置客户端最多接受未被ack的消息个数
        channel.basicQos(64);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("recv message: " + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUE_NAME, consumer);
        // 等待回调函数执行完毕之后，关闭资源
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}
