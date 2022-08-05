package com.christinagorina.util;

public class Util {
    public static int getRandomeInt(){
        return (int)(Math.random() * (2));
    }

    public static void getRandomeSleep() throws InterruptedException {
        Thread.sleep((long)(Math.random() * 1000));
    }



}
