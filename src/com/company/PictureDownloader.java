package com.company;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.company.Main.page;

public class PictureDownloader extends Thread implements Regulars{

    URL pageUrl;
    String fullPath;
    String link;

    public PictureDownloader(String url) throws MalformedURLException {
        this.pageUrl = new URL(url);
    }

    @Override
    public void run() {
        super.run();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pageUrl.openStream()))){
            page = bufferedReader.lines().collect(Collectors.joining("\n"));

            Pattern email_pattern = Pattern.compile(REGULAR_FOR_PICTURE_DOWNLOAD);
            Matcher matcher = email_pattern.matcher(page);
            matcher.find();

            URL url = new URL(matcher.group());
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n;

            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }

            out.close();
            in.close();

            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream(fullPath + "cover.jpg");

            fos.write(response);
            fos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
