package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Downloader extends Thread{

    private final static String REGULAR_FOR_MUSIC_DOWNLOAD = "\\/track\\/dl(.+).mp3";
    private final static String REGULAR_FOR_PICTURE_DOWNLOAD = "https:\\/\\/40s.musify.club\\/img\\/(.+).jpg";
    private final static String REGULAR_FOR_MUSIC_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String REGULAR_FOR_PICTURE_NAME = "https:\\/\\/40s.musify.club\\/img\\/69\\/";
    private final static String PATH = "music\\";
    private final static String PROTOCOL = "https://musify.club";

    URL pageUrl;
    String page;
    String fullPath;

    public Downloader(String url) throws MalformedURLException {
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
            musicDownload(link, result);
            pictureDownload();

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

    private void pictureDownload() throws IOException {
        Pattern email_pattern = Pattern.compile(REGULAR_FOR_PICTURE_DOWNLOAD);
        Matcher matcher = email_pattern.matcher(page);
        matcher.find();

        URL url = new URL(matcher.group());
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;

        while (-1!=(n=in.read(buf)))
        {
            out.write(buf, 0, n);
        }

        out.close();
        in.close();

        byte[] response = out.toByteArray();
        System.out.println(fullPath + matcher.group().replaceAll(REGULAR_FOR_PICTURE_NAME, ""));
        FileOutputStream fos = new FileOutputStream(fullPath + "cover.jpg");

        fos.write(response);
        fos.close();
    }
}
