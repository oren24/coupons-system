package com.oren.coupons.dto;

import com.oren.coupons.entities.PurchaseEntity;

import java.sql.Date;

public class Purchase {



    private int id;
    private int couponId;
    private int userId;
    private int amount;
    Date date;
    public Purchase() {

    }

    public Purchase(int couponId, int userId, int amount, Date date) {
        this.couponId = couponId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
    }

    public Purchase(int id, int couponId, int userId, int amount, Date date) {
        this(couponId, userId, amount, date);
        this.id = id;
    }
    public Purchase(PurchaseEntity purchaseEntity){
        this.id = purchaseEntity.getId();
        this.couponId = purchaseEntity.getCoupon().getId();
        this.userId = purchaseEntity.getUser().getId();
        this.amount = purchaseEntity.getAmount();
        this.date = purchaseEntity.getDate();
    }



    public int getId() {
        return id;
    }

    public int getCouponId() {
        return couponId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "Id=" + id +
                ", couponId=" + couponId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }

    public void setUserId(Integer id) {
    }


}
