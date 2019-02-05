package com.hsyou.wagupost.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsyou.wagupost.model.PostDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProvider {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Value("${kafka.topic.post}")
    private String postTopicName;

    @Autowired
    private KafkaProducer<String, String> kafkaProducer;

    public void sendPostMessage(PostDTO postDTO){
        try {
            String value = OBJECT_MAPPER.writeValueAsString(postDTO);

            log.info("send "+value);
            kafkaProducer.send(new ProducerRecord<>(postTopicName, value));

        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    }
}