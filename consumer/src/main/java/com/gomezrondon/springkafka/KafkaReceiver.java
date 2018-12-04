package com.gomezrondon.springkafka;

import com.gomezrondon.springkafka.service.TextProcessorService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static reactor.core.publisher.Flux.just;

@Component
public class KafkaReceiver {

    private final TextProcessorService textProcessorService;

    public KafkaReceiver(TextProcessorService textProcessorService) {
        this.textProcessorService = textProcessorService;
    }

    @KafkaListener(topics = "topic1")
    public void receiveTopic1(ConsumerRecord<?, ?> consumerRecord) {
        System.out.println("Receiver on topic1: "+consumerRecord.toString());
    }

    @KafkaListener(topics = "test-topic")
    public void receiveTopic2(ConsumerRecord<?, ?> consumerRecord) {
        textProcessorService.processHtmlLogLine(just((String)consumerRecord.value()));
      //  System.out.println("Receiver on topic2: "+consumerRecord.value());
    }

}