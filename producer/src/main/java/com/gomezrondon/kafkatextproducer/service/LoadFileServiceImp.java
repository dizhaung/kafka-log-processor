package com.gomezrondon.kafkatextproducer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoadFileServiceImp implements LoadFileService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void readHtmlLogFile(String fileName) throws IOException {

        readFile(fileName).doOnNext(x -> {
            kafkaTemplate.send("test-topic",x);
        }).subscribe();

    }

    private static Flux<String> fromPath(Path path) {
        return Flux.using(() -> Files.lines(path),
                Flux::fromStream,
                BaseStream::close
        );
    }


    private static List<Path> getPathOfFilesInFolder(String folder) throws IOException {

        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            return paths
                    .filter(Files::isRegularFile)
                    //.peek(System.out::println)
                    .collect(Collectors.toList());
        }
    }


     public Flux<String> readFile(String file) throws IOException {

          if(file != null){
              return  fromPath(Paths.get(file));
         }

        return null;
     }

}
