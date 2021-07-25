package com.example.demo.tjw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private TjwRestClient restClient;
    //3.添加定时任务
    @Scheduled(cron = "0 0/2 * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        String response = restClient.getFarm();
        if(response==null) {
            restClient.sendWarn("15966604510","谭家湾数据Web异常！");
        }
        TjwResponse response2 = restClient.get();
        if(response2==null) {
            restClient.sendWarn("18660775196","谭家湾控制服务异常！");
        }
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}