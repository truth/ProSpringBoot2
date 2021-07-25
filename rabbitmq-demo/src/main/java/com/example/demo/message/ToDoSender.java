package com.example.demo.message;

import com.example.demo.entity.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
public class ToDoSender {
    @Autowired
    private ToDoProducer producer;
    @Value("${todo.amqp.queue}")
    private String destination;
    private static int i=0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Bean
    public CommandLineRunner sendToDos(@Value("${todo.amqp.queue}") String
                                               destination, ToDoProducer producer){
        return args -> {
            producer.sendTo(destination,new ToDo("workout tomorrow morning!",0));
        };
    }
    @Scheduled(fixedRate = 50000L)
    private void sendToDos(){
        producer.sendTo(destination,new ToDo("Thinking on Spring Boot at "
                + dateFormat.format(new Date()),i++));
    }
}