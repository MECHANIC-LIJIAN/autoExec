package com.magic.ssh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String sayHello(){
        return "hello world";
    }

    @RequestMapping("/api/hello")
    public String apiHello(){
        return "hello world";
    }

    @RequestMapping("/mem/hello")
    public String memHello(){
        return "hello world";
    }

    @RequestMapping("/no/hello")
    public String noHello(){
        return "hello world";
    }
}
