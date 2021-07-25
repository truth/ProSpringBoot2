package com.example.demo.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Random;

@Configuration
public class MqttConfiguration {
 
	private Random rand = new Random();
 
	private String clientId = "mqtt_scheduling_client";//""
	
	@Value("${mqtt.server}")
    private String server;
	
	@Value("${mqtt.username}")
    private String username;
	
	@Value("${mqtt.password}")
    private String password;
	
	// ==================== 连接activemq ====================
	@Bean
	public MqttPahoClientFactory clientFactory() {
		DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		String[] uris = new String[1];
		uris[0] = server;//"tcp://localhost:1883";
		options.setServerURIs(uris);
		if(username!=null && !username.equals("")){
			options.setUserName(username);
			options.setPassword(password.toCharArray());
		}		
		clientFactory.setConnectionOptions(options);
		return clientFactory;
	}
 
	// ==================== 生产者 ====================
	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}
 
	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel") // 绑定生产者
	public MqttPahoMessageHandler mqttOutbound(MqttPahoClientFactory clientFactory) {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
				clientId + String.format("%05d", rand.nextInt(10000)), clientFactory);
		messageHandler.setAsync(true);
		messageHandler.setDefaultQos(0);
		messageHandler.setDefaultRetained(false);
		messageHandler.setAsyncEvents(true);
		return messageHandler;
	}
 
	// ==================== 消费者 ====================
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}
 
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel") // 绑定消费者
	public MessageHandler handler() {
		return new ReceiveMessageHandler();
	}
 
	@Bean
	public MessageProducerSupport mqttInbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				clientId + String.format("%05d", rand.nextInt(10000)), clientFactory(), "mqtt-topic-rule");
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}
 
}