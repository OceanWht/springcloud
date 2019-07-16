package com.wht.eurekaclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "index")
    public String sayHi(){
        return "Hello World！，端口： "+port;
    }
}
