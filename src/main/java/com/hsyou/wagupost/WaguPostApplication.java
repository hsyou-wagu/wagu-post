package com.hsyou.wagupost;

import com.hsyou.wagupost.service.SimpleKafkaConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@Slf4j
@SpringBootApplication
public class WaguPostApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WaguPostApplication.class, args);
    }

    @Value("${kafka.bootstrap.servers}")
    private String kafkaBootstrapServers;


    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${kafka.consumer.group.id}")
    private String kafkaGroupId;
    @Value("${kafka.topic.post}")
    private String postTopicName;
    @Override
    public void run(String... args) throws Exception {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", kafkaBootstrapServers);
        consumerProperties.put("group.id", kafkaGroupId);
        consumerProperties.put("zookeeper.session.timeout.ms", "6000");
        consumerProperties.put("zookeeper.sync.time.ms","2000");
        consumerProperties.put("auto.commit.enable", "false");
        consumerProperties.put("auto.commit.interval.ms", "1000");
        consumerProperties.put("consumer.timeout.ms", "-1");
        consumerProperties.put("max.poll.records", "1");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Thread kafkaConsumerThread = new Thread(() -> {
            log.info("Starting Kafka consumer thread.");


            SimpleKafkaConsumer simpleKafkaConsumer = new SimpleKafkaConsumer(
                    postTopicName,
                    consumerProperties
            );

            simpleKafkaConsumer.runSingleWorker();
        });

        /*
         * Starting the first thread.
         */
        kafkaConsumerThread.start();
    }
}

