package com.oren.coupons.TimerTasks;

import com.oren.coupons.dto.Coupon;
import com.oren.coupons.logic.CouponLogic;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.TimerTask;

public class ExpiredCouponsTimerTask extends TimerTask implements Runnable {

    private CouponLogic couponLogic;

    public ExpiredCouponsTimerTask() {
        this.couponLogic = couponLogic;
    }

    @Override
    public void run() {
        try {
            // Get the current date and time.
            Timestamp currentDate = new Timestamp(System.currentTimeMillis());
            Date currentDate2 = new Date(System.currentTimeMillis());
            // Check if it is midnight.
            if (currentDate.getHours() == 0 && currentDate.getMinutes() == 0 ) {
                // Get all coupons that have expired.
                List<Coupon> expiredCoupons = this.couponLogic.getAllCouponsByEndDate(currentDate2);

                // Delete all expired coupons.
                for (Coupon coupon : expiredCoupons) {
                    if (coupon.getEndDate().before(currentDate2)) {
                        this.couponLogic.deleteCoupon(coupon.getId());
                    }
                }
            } else {
                // Sleep until midnight.
                Thread.sleep(1000 * 60 * 60 * 24 - (System.currentTimeMillis() - currentDate.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();

    }}

    @Override
    public long scheduledExecutionTime() {
        return System.currentTimeMillis() + 1000 * 60 * 60 * 24;
    }

    public void startOnDifferentThread() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
