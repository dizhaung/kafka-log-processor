package com.gomezrondon.springkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class SpringKafkaApplication{


	@Autowired
	KafkaSender kafkaSender;

	@PostMapping("/kafka/{topicName}")
	public String sendToTopic(@PathVariable String topicName, @RequestBody String message) {
		kafkaSender.send(topicName, message);
		return "Message sent";
	}

	@GetMapping
	public String getMethod(){
		return "Date Time is:"+new Date();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaApplication.class, args);
	}


}
