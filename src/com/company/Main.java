package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private final static String REGULAR_FOR_MUSIC_DOWNLOAD = "\\/track\\/dl(.+).mp3";
    private final static String REGULAR_FOR_PICTURE_DOWNLOAD = "https:\\/\\/(.+).jpg";
    private final static String REGULAR_FOR_MUSIC_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String PATH = "music\\";
    private final static String PROTOCOL = "https://musify.club";

    static URL pageUrl;
    static String page;
    static String fullPath;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pattern email_pattern = Pattern.compile(REGULAR_FOR_MUSIC_DOWNLOAD);
        String line;

        while (true) {

            line = scanner.nextLine();
            if (line.equals("End")) {
                break;
            }

            try {
                pageUrl = new URL(line);
            }catch (IOException ex){
                System.out.println(ex.getMessage());
            }

            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
                page = bufferedReader.lines().collect(Collectors.joining("\n"));
                Matcher matcher = email_pattern.matcher(page);
                matcher.find();
                String link = matcher.group();
                String result = link.replaceAll(REGULAR_FOR_MUSIC_NAME, "");

                fullPath = PATH + result.replaceAll(".mp3", "") + "\\";

                File file = new File(PATH);
                file.mkdir();

                file = new File(fullPath);
                file.mkdir();

                //new MusicDownloader(line, link, fullPath).start();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
