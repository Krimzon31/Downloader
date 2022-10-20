package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PictureDownloader extends Thread{

    private final static String REGULAR_FOR_PAGE = "\\/track\\/dl(.+).mp3";
    private final static String REGULAR_FOR_PICTURE_DOWNLOAD = "https:\\/\\/(.+).jpg";
    private final static String REGULAR_FOR_MUSIC_NAME = "\\/track\\/dl\\/(.+)\\/";
    private final static String PATH = "music\\";
    private final static String PROTOCOL = "https://musify.club";

    URL pageUrl;
    String page;
    String fullPath;

    public PictureDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        super.run();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR_FOR_PAGE);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();
            String link = matcher.group();
            String result = link.replaceAll(REGULAR_FOR_MUSIC_NAME, "");

            createFolder(result);
            pictureDownload();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        FileOutputStream fos = new FileOutputStream(fullPath + "cover.jpg");
        //FileOutputStream fos = new FileOutputStream(fullPath + matcher.group().replaceAll(REGULAR_FOR_PICTURE_NAME, ""));

        fos.write(response);
        fos.close();
    }

    private void createFolder(String result){
        fullPath = PATH + result.replaceAll(".mp3", "") + "\\";

        File file = new File(PATH);
        file.mkdir();

        file = new File(fullPath);
        file.mkdir();
    }
}
