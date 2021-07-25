package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    /**
     * 设置数据，返回到freemarker视图
     * @return
     */
    @RequestMapping("/auth")
    public String say(){
        return "ok";
    }
}
