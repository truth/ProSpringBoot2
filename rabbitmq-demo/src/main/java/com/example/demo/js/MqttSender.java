package com.example.demo.js;


import com.example.demo.SpringContextUtils;
import com.example.demo.config.MqttMessageGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class MqttSender {

	private static MqttMessageGateway gateway;
    public void send(String topic, String payload) {
        if(gateway==null){
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            gateway = SpringContextUtils.getBean(MqttMessageGateway.class);
        }
        Message<String> message = MessageBuilder.withPayload(payload)
                .setHeader(MqttHeaders.TOPIC, topic).build();
        gateway.sendMessage(message);
    }
}
