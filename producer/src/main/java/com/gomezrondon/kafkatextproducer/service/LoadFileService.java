package com.gomezrondon.kafkatextproducer.service;


import reactor.core.publisher.Flux;

import java.io.IOException;

public interface LoadFileService {

    Flux<String> readFile(String file) throws IOException;

    void readHtmlLogFile(String fileName) throws IOException;
}
