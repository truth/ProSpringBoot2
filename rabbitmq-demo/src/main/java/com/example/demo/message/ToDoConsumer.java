package com.example.demo.message;

import com.example.demo.entity.ToDo;
import com.example.demo.repository.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ToDoConsumer {
    private Logger log = LoggerFactory.getLogger(ToDoConsumer.class);
    private ToDoRepository repository;
    public ToDoConsumer(ToDoRepository repository){
        this.repository = repository;
    }
    //  queues = "${todo.amqp.queue}",
    //  @RabbitListener can have only one of 'queues', 'queuesToDeclare', or 'bindings'
    @RabbitListener(bindings ={@QueueBinding(key = "${todo.amqp.queue}",value = @Queue(value = "${todo.amqp.queue}",durable = "true"),
            exchange =@Exchange(value = "amq.topic",type="topic",durable = "true"))})
    public void processToDo(ToDo todo){
        log.info("Consumer> " + todo);
        log.info("ToDo created> " + this.repository.save(todo));
    }
}