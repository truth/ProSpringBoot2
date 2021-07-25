package com.example.demo.js;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyTest  implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String,  String> redisTemplate;
    @Override
    public void  run(String... strings) throws Exception {
        JsContextFactory orderFactory = new JsContextFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5);
        //设置获取连接超时时间
        config.setMaxWaitMillis(1000);
        JsContextPool jsCtxPool = new JsContextPool(orderFactory, config);
        //redisTemplate.opsForValue().set();
        for (int i = 0; i < 2; i++) {
            JsContext o = jsCtxPool.borrowObject();
            o.register("redis",redisTemplate);
            o.log("i="+i);
            JSONObject obj = new JSONObject();
            obj.put("jscode","function main(){" +
                    "    redis.opsForValue().set('hello','javascript engine!');" +
                    "    var json = JSON.parse('{\"age\":30}');" +
                    "    Console.println(\"Hello \"+json.age);" +
                    "    var now = new Date();Console.println('Date:'+now);" +
                    "    for(var i=0;i<1;i++){var payload={};\n" +
                    "    payload.id='1001';\n" +
                    "    payload.slaveId=i;\n" +
                    "    payload.chn=i;\n" +
                    "    payload.val=1;\n" +
                    "    MqttSender.send(\"/gk/tjw/alarm2\",JSON.stringify(payload));" +
                    "    }\n" +
                    "    return null;\n" +
                    "}");
            o.doTopicRule(obj,null);
            System.out.println("brrow a connection: " + o +" active connection:"+jsCtxPool.getNumActive());
            jsCtxPool.returnObject(o);
        }
    }
}
