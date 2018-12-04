package com.gomezrondon.kafkatextproducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.stream.IntStream;

@SpringBootApplication
public class KafkaTextProducerApplication implements CommandLineRunner {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(KafkaTextProducerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		IntStream.range(5,10).forEach(i ->{
			kafkaTemplate.send("test-topic",Integer.toString(i),"body:"+i);
		});


	}

}
