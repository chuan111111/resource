package com.example.backend.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Hellotest {
    @RequestMapping("/hello")
    public String hello(){
        return "Hello world";
    }
}
