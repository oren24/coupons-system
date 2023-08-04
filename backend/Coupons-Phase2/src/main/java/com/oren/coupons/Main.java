package com.oren.coupons;



import com.oren.coupons.TimerTasks.ExpiredCouponsTimerTask;
import com.oren.coupons.dal.IUsersDal;
import com.oren.coupons.dto.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        // Start the expired coupons timer task.
        ExpiredCouponsTimerTask expiredCouponsTimerTask = new ExpiredCouponsTimerTask();
        expiredCouponsTimerTask.startOnDifferentThread();

        // Start the Spring Boot application.
        SpringApplication.run(Main.class, args);


    }


}