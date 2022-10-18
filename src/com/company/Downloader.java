package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract public class Downloader extends Thread{
    static String REGULAR_FOR_DOWNLOAD;
    static String REGULAR_FOR_NAME = "\\/track\\/dl\\/(.+)\\/";
    static String PATH = "music\\";
    static String PROTOCOL = "https://musify.club";

    URL pageUrl;
    String page;
    String fullPath;

    public String init(){
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR_FOR_DOWNLOAD);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();
            return matcher.group();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String createFolder(String link){
        fullPath = PATH + link.replaceAll(REGULAR_FOR_NAME, "").replaceAll(".mp3", "") + "\\";

        File file = new File(PATH);
        file.mkdir();

        file = new File(fullPath);
        file.mkdir();

        return fullPath;
    }

    public void Download(String link){

    }
}



