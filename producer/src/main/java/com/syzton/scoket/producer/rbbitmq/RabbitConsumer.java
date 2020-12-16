package com.syzton.scoket.producer.rbbitmq;

import com.rabbitmq.client.Channel;
import com.syzton.scoket.producer.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @program: teaching
 * @description:
 * @author: fengxx
 * @create: 2020-12-15 16:24
 **/
@Component
public class RabbitConsumer {

    private static RabbitConsumer rabbitConsumer;
    @Resource
    private WebSocketServerEndpoint webSocketServerEndpoint; //引入WebSocket

    /**
     * 构造方法注入rabbitTemplate
     */
    @PostConstruct
    public void init() {
        rabbitConsumer = this;
        rabbitConsumer.webSocketServerEndpoint = webSocketServerEndpoint;
    }

    @RabbitListener(queues = RabbitConfig.msg_queue) //监听队列
    public void msgReceive(String content, Message message, Channel channel) throws IOException {
        System.out.println("----------------接收到消息--------------------"+content);
        //发送给WebSocket 由WebSocket推送给前端
        rabbitConsumer.webSocketServerEndpoint.sendMessageOnline(content);
        // 确认消息已接收
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
