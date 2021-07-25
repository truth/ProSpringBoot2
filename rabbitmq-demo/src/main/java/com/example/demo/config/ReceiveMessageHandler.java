package com.example.demo.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public class ReceiveMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
    }
}
