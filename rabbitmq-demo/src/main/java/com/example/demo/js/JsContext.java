package com.example.demo.js;

import javax.script.*;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.SpringContextUtils;
import com.example.demo.config.MqttMessageGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

public class JsContext {

	private static 	ScriptEngineManager manager = new ScriptEngineManager();
	private static  ScriptEngine engine = null;	
	
	@Autowired
	private MqttMessageGateway gateway;
	
	static {
		if(engine == null) {
			engine = manager.getEngineByName("js");
			engine.put("Console",System.out);
			engine.put("MqttSender",new MqttSender());
		}
	}	
	public void register(String var, Object obj) {
		engine.put(var,obj);
	}
	public synchronized double doCalc(double i, String formula) {
		synchronized(engine) {
			engine.put("value", i);
			try {
				Object result = engine.eval(formula);
				if (result instanceof Double) {
					return (Double) result;
				}else if (result instanceof Integer) {
					return (Integer) result;
				}
			} catch (ScriptException e1) {
				e1.printStackTrace();
			}// 对文件或其它外部资源中得脚本表达式进行求值
		}
		return 0;

	}
	public void log(String msg)  {
		try
		{
			engine.eval("Console.println('I`m Console.println."+msg+"')");
		} catch (ScriptException e1) {
			e1.printStackTrace();
		}
		try {
			engine.eval("var a=1+2;Console.println(a);function main(msg){Console.println(msg);}");
			if (engine instanceof Invocable) {
				Invocable invoke = (Invocable) engine;
				String result = (String) invoke.invokeFunction(
						"main",
						"java arguments");
				System.out.println(result);
			} else {
				System.out.println("error");
			}
		} catch (ScriptException | NoSuchMethodException e) {
			System.out.println("表达式runtime错误:" + e.getMessage());
		}
	}	
	//执行规则引擎脚本
	public String doTopicRule(JSONObject obj,JSONObject objMsg)  {	
		
		synchronized(engine) {
			
			
			String jsCode = obj.getString("jscode");
			String result = "";	
			String outTopic = "";
			if(obj.get("outtopic") != null && !obj.getString("outtopic").equals("")){
				outTopic = obj.getString("outtopic");
			}			
			
			try {
			
				engine.eval(jsCode);
				if (engine instanceof Invocable) {
					Invocable invoke = (Invocable) engine;
				    result = (String) invoke.invokeFunction(
							"main",
							objMsg);
					
					if(result!=null && !outTopic.equals("")){					
						
						if(gateway==null){
							SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
							gateway = SpringContextUtils.getBean(MqttMessageGateway.class);
						}
						Message<String> message = MessageBuilder.withPayload(result)
				                .setHeader(MqttHeaders.TOPIC, outTopic).build();
				        gateway.sendMessage(message);
					}
					
				} else {
					System.out.println("error");
				}
			} catch (ScriptException | NoSuchMethodException e) {
				System.out.println("表达式runtime错误:" + e.getMessage());
			}
			
			return result;
		}		
		
	}
	
	public static void main(String[] args) {
		JsContext js =  new JsContext();
//		js.log("Demo");
//		long b,e;
//		
//		for(int i=0;i<10;i++)
//		{
//			b = System.currentTimeMillis();
//			for(int j=0;j<10000;j++)
//			{
//				js.doCalc(5.0+(((i+j)%10)/10.0), "(value-4)*100");
//			}
//			e = System.currentTimeMillis();
//			System.out.println("total's time(ms):"+(e-b)+", per second:"+10000*1000/(e-b));
//		}
		
		JSONObject obj = new JSONObject();
		JSONObject objMsg = new JSONObject();		
		obj.put("jscode", "function main(msg){ return msg.id;} ");
		objMsg.put("id", "1111111");
		objMsg.put("cmd", "hello");
		js.doTopicRule(obj,objMsg);
		
	}
}
