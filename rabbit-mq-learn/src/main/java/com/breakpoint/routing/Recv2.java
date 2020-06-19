package com.breakpoint.routing;

import java.io.IOException;

import com.breakpoint.WorkQueueTest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Recv2 {

	private final static String QUEUE_NAME = "direct_exchange_queue_email";// 邮件队列
	private final static String EXCHANGE_NAME = "test_direct_exchange";

	public static void main(String[] args) throws Exception {
		
		Connection connection = WorkQueueTest.getConnection();
		
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		// // 绑定队列到交换机，同时指定需要订阅的routing key。可以指定多个
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "email");//指定接收发送方指定routing key为email的消息
		
		// 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                // body 即消息体
                String msg = new String(body);
                System.out.println(" [邮件服务] received : " + msg + "!");
            }
        };
        // 监听队列，自动ACK
        channel.basicConsume(QUEUE_NAME, true, consumer);
	
	}

}
