package com.example.agricolab;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AgricolabController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello!";
    }

}
