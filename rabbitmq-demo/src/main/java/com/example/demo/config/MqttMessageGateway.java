package com.example.demo.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
 
/**
 * 消息发送接口，不需要实现，spring会通过代理的方式实现
 */
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttMessageGateway {
	void sendMessage(Message<?> message);
}