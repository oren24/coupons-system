package com.oren.coupons.utils;

import com.oren.coupons.threads.SendStatisticsThread;

public class StatisticsUtils extends Thread {
    /*todo:
       BE advised:!!
       Statistics will be sent as a  MICRO SERVICE in the future!!!
       until then: this class will ONLY print their orders & methods */
    private String text;

    public StatisticsUtils(String text) {
        this.text = text;
    }


    public  static void sendStatistics(String text){
        SendStatisticsThread sendStatisticsThread = new SendStatisticsThread(text);
        sendStatisticsThread.run();
    };
}
