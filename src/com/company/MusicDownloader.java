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

public class MusicDownloader extends Downloader{

    URL pageUrl;
    String fullPath;
    String link;

    public MusicDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        try {
            super.run();
            link = init();
            fullPath = super.createFolder(link);
            ReadableByteChannel byteChannel = Channels.newChannel(new URL(PROTOCOL + link).openStream());

            FileOutputStream stream = new FileOutputStream(fullPath + link);
            stream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);

            stream.close();
            byteChannel.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void Download(String link) {

    }
}
