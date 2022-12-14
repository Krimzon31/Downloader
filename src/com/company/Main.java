package com.company;

import javazoom.jl.player.Player;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line;

        System.out.println("Вводите ссылки для скачивания музыки и картинки по очереди. Для окончания процедуры введите \"End\"");

        try {
            while (true) {
                line = scanner.nextLine();
                if(line.equals("End")){
                    break;
                }
                new MusicDownloader(line).start();
                new PictureDownloader(line).start();
            }
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            System.out.println("Введены некоректные данные");
        }
    }
}
