package com.example.demo.message;

import com.example.demo.entity.ToDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ToDoProducer {
    private static final Logger log = LoggerFactory.getLogger(ToDoProducer.
            class);
    private RabbitTemplate template;
    public ToDoProducer(RabbitTemplate template){
        this.template = template;
    }
    public void sendTo(String queue, ToDo toDo){
        this.template.convertAndSend("amq.topic",queue,toDo);
        log.info("Producer> Message Sent");
    }
}