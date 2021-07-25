package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.example.demo.tjw.TjwResponse;
import com.example.demo.tjw.TjwRestClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@NacosPropertySource(dataId = "example", autoRefreshed = true)
@EnableDiscoveryClient
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@Bean
	public CommandLineRunner process(TjwRestClient client){
		return args -> {
			TjwResponse response = client.get();
			System.out.println(JSON.toJSONString(response));
		};
	}
}
