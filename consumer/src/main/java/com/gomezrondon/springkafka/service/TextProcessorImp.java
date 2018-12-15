package com.gomezrondon.springkafka.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextProcessorImp implements TextProcessorService {

    private List<String> tarfilesProcessed = new ArrayList<>();
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLACK = "\u001B[30m";



    public Flux<String> dataLoadStatFormat(String line){


        if(line.length() > 154) return Flux.just("line longer than 500 characters");
  //      System.out.println("line: "+line);

        String[] var = line.split(" ");
        String dataLoadDate = var[0]+var[1].substring(0,8);;
        String pattern1 = "\\d{14}";
        String pattern2 = "(?<=download\\/).*";
        String pattern3 = "\\s(\\d*)\\s";
        String dataFeedString = getPattern(line, pattern1); //\s(\d*)\s
        String tarFileName = getPattern(line, pattern2);
        String tranCode = getPattern(line, pattern3).trim();
        String paddedTranCode = String.format("%8s", tranCode);
        String datePatter1 = "yyyMMddHHmmss";
        String datePatter2 = "yyy-MM-ddHH:mm:ss";
        LocalDateTime dateTime = convertStringToDate(dataLoadDate, datePatter2);
        LocalDateTime dateTime2 = convertStringToDate(dataFeedString, datePatter1);
        String paddeddataLoadDate = addPaddingToDate(dateTime);
        String paddeddataFeedDate = addPaddingToDate(dateTime2);
        String differenceInHourMinutes = getDifferenceBetweenDates(dateTime, dateTime2);
        String paddedDifference = String.format("%5s", differenceInHourMinutes);
        //		System.out.println(">> "+dataFeedTime[0]);
        String duplicated = isDuplicated(tarfilesProcessed, tarFileName);
        tarfilesProcessed.add(tarFileName);
        String paddedSize = getIndex(tarfilesProcessed);

        return Flux.just(String.join("~",paddedSize,paddeddataLoadDate,paddedTranCode,paddeddataFeedDate,paddedDifference,tarFileName,duplicated));
    }



    private String getIndex(List<String> tarfilesProcessed) {
        int size = tarfilesProcessed.size();
        return String.format("%4s", size);
    }

    private String isDuplicated(List<String> tarfilesProcessed, String tarFileName) {
        String duplicated = "";
        if(tarfilesProcessed.contains(tarFileName)){
            duplicated = "*";
        }
        return duplicated;
    }

    private String getDifferenceBetweenDates(LocalDateTime dateTime, LocalDateTime dateTime2) {
        int t = (int) ChronoUnit.MINUTES.between(dateTime2, dateTime);
        int hours = t / 60; //since both are ints, you get an int
        int minutes = t % 60;
        return formatHourMinute(hours, minutes);
    }

    private String formatHourMinute(int hours, int minutes) {
        return String.format("%d:%02d", hours, minutes);
    }

    private String addPaddingToDate(LocalDateTime localDateTime){
        return String.format("%-19s", localDateTime);
    }

    private LocalDateTime convertStringToDate(String dataFeedString, String datePatter1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePatter1);
        return LocalDateTime.parse(dataFeedString, formatter);
    }

    private String getPattern(String line, String pattern) {
        Pattern MY_PATTERN = Pattern.compile(pattern);
        Matcher m = MY_PATTERN.matcher(line);
        if (m.find()) {
            return m.group();
        }
        return null;
    }


/*
    private void processHtmlLogFile(String fileName) throws IOException {
        System.out.println(ANSI_YELLOW + "********************" + ANSI_YELLOW);


        if(fileName != null){
            processHtmlLogLine(service.readFile(fileName));
        }

        System.out.println(ANSI_YELLOW + "********************" + ANSI_YELLOW);
        System.out.println(ANSI_BLACK + "" + ANSI_BLACK);
    }
*/

    public void processHtmlLogLine(Flux<String> fileFlux ) {
        final String regex = "\">(.*)<";

         fileFlux
                .skipWhile(s -> !s.contains("UncompressTarResource"))
                .map(String::trim)
                .map(line -> getRegexPatterMatch(regex,line))
                .flatMap(this::dataLoadStatFormat)
                .subscribe(System.out::println);

		/* Output example
		   1~2018-11-08T00:03:46~ 9411399~2018-11-07T16:15:04~ 7:48~pw-dataload-20181107161504-uat-3.7.tar~
		   2~2018-11-08T00:08:19~ 9684080~2018-11-07T16:15:04~ 7:53~pw-dataload-20181107161504-uat-3.7.tar~*
		   3~2018-11-08T00:12:54~ 9958828~2018-11-07T17:49:45~ 6:23~pw-dataload-20181107174945-uat-3.7.tar~
		   4~2018-11-08T00:17:05~10209506~2018-11-07T17:29:45~ 6:47~pw-dataload-20181107172945-uat-3.7.tar~
		*/

    }


    private String getRegexPatterMatch(String regex, String line){
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
