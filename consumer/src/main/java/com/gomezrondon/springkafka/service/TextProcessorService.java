package com.gomezrondon.springkafka.service;

import reactor.core.publisher.Flux;

public interface TextProcessorService {

    Flux<String> dataLoadStatFormat(String line);

    void processHtmlLogLine(Flux<String> fileFlux);

  // void processHtmlLogFile(String fileName) throws IOException;

}
