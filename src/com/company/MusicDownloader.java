package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MusicDownloader extends Thread{
    private final static String REGULAR_FOR_MUSIC_DOWNLOAD = "\\/track\\/dl(.+).mp3";
    private final static String REGULAR_FOR_MUSIC_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String PATH = "music\\";
    private final static String PROTOCOL = "https://musify.club";

    URL pageUrl;
    String page;
    String fullPath;

    public MusicDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
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

            createFolder(result);
            System.out.println("Folder is created");
            musicDownload(link, result);
            System.out.println("Music is downloaded");
            new OurPlayer(pageUrl).start();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void createFolder(String result){
        fullPath = PATH + result.replaceAll(".mp3", "") + "\\";

        File file = new File(PATH);
        file.mkdir();

        file = new File(fullPath);
        file.mkdir();
    }

    private void musicDownload(String link, String result) throws IOException {
        ReadableByteChannel byteChannel = Channels.newChannel(new URL(PROTOCOL + link).openStream());
        FileOutputStream stream = new FileOutputStream(fullPath + result);
        stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);

        stream.close();
        byteChannel.close();
    }
}
