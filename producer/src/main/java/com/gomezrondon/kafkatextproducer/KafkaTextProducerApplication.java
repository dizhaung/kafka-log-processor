package com.gomezrondon.kafkatextproducer;

import com.gomezrondon.kafkatextproducer.service.LoadFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaTextProducerApplication implements CommandLineRunner {


	private final LoadFileService loadFileService;

	@Value("${working.dierectory}")
	private String workingDirectory;

	public KafkaTextProducerApplication(LoadFileService loadFileService) {
		this.loadFileService = loadFileService;
	}


	public static void main(String[] args) {
		SpringApplication.run(KafkaTextProducerApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		loadFileService.readHtmlLogFile(workingDirectory);

	}

}
