package com.oren.coupons.threads;

import java.sql.Date;
import java.sql.Timestamp;

public class SendStatisticsThread extends Thread{
    private String text;
    //private int id;
    private Timestamp timestamp;
    public SendStatisticsThread(String text) {
        this.text = text;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    public void run(){
        try{
            Thread.sleep(2000);
            System.out.println("sleep over");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.timestamp+": "+this.text);

    }
}
