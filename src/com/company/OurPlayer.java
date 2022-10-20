package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OurPlayer extends Thread{

    private final static String REGULAR_FOR_MUSIC_DOWNLOAD = "\\/track\\/dl(.+).mp3";
    private final static String REGULAR_FOR_MUSIC_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String PATH = "music\\";

    URL pageUrl;
    String page;
    String fullPath;

    public OurPlayer(URL url) throws MalformedURLException {
        this.pageUrl = url;
    }

    @Override
    public void run() {
        super.run();

        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR_FOR_MUSIC_DOWNLOAD);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();
            String link = matcher.group();
            String result = link.replaceAll(REGULAR_FOR_MUSIC_NAME, "");

            fullPath = PATH + result.replaceAll(".mp3", "") + "\\";

            try (FileInputStream inputStream = new FileInputStream( fullPath + result)) {
                System.out.println("Audio " + result + "was started");
                try {
                    Player player = new Player(inputStream);
                    player.play();
                } catch (JavaLayerException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
