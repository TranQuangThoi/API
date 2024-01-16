package com.techmarket.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RabbitMQService{
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitSender rabbitSender;

    public void createQueueIfNotExist(String queueName){
        rabbitSender.createQueueIfNotExist(queueName);
    }
}
