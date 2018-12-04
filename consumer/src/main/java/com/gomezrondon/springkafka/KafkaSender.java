package com.gomezrondon.springkafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        kafkaTemplate.send(topic, payload);
      //  System.out.println("Message: "+payload+" sent to topic: "+topic);
    }

    @KafkaListener(topics = "topic1")
    public void receiveTopic1(ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("Receiver on topic1: "+consumerRecord.toString());
    }

    @KafkaListener(topics = "test-topic")
    public void receiveTestTopic(ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("Receiver on topic1: "+consumerRecord.toString());
    }

}