package com.syzton.scoket.producer.rbbitmq;

import com.alibaba.fastjson.JSONObject;
import com.syzton.scoket.producer.bean.MsgBean;
import com.syzton.scoket.producer.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * @program: teaching
 * @description:
 * @author: fengxx
 * @create: 2020-12-15 15:58
 **/
@Slf4j
@Component("rabbitProductss")
public class RabbitProduct implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public RabbitProduct(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }



    //发送消息 推送到websocket    参数自定义 转为String发送消息
    public void sendMSG(MsgBean msg){
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        //把消息对象放入路由对应的队列当中去
        rabbitTemplate.convertAndSend(RabbitConfig.msg_exchang,RabbitConfig.msg_routing_key, JSONObject.toJSON(msg).toString(), correlationId);
    }

    /**
     * 发送成功回调方法
     * @param correlationData
     * @param b
     * @param s
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("消息唯一标识----->"+correlationData.toString());
        log.info("确认结果---->"+ack);
        log.info("失败原因-----"+cause);

    }


    /**
     * 失败返回结果
     * @param returnedMessage
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.debug("消息发送失败------>");
        log.debug(returnedMessage.toString());

    }
}
