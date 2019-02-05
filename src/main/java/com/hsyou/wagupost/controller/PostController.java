package com.hsyou.wagupost.controller;

import com.hsyou.wagupost.model.PostDTO;
import com.hsyou.wagupost.service.KafkaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    @Autowired
    private KafkaProvider kafkaProvider;
    @GetMapping("/health")
    public String health(){
        return "hello!";

    }

    @PostMapping("/")
    public String kafkaTest(){
        System.out.println("kafka!");
        kafkaProvider.sendPostMessage(PostDTO.builder().contents("aa").build());
        return "ok";
    }
}
