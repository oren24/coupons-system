package com.oren.coupons.dto;

import com.oren.coupons.entities.PurchaseEntity;

import java.sql.Date;

public class PurchaseExtended extends Purchase{
    private String couponName;
    private String couponDescription;
    private Date couponStartDate;
    private Date couponEndDate;
    private int couponCategory;
    private float price;
    private String username;
    private String companyName;

    private String contactEmail;
    public PurchaseExtended() {
    }
    public PurchaseExtended(Integer id, Integer couponId, Integer userId, Integer amount, Date date) {
        super(id, couponId, userId, amount, date);
    }
    public PurchaseExtended(Integer couponId, Integer userId, Integer amount, Date date) {
        super(couponId, userId, amount, date);
    }
    public PurchaseExtended(Integer id, Integer couponId, Integer userId, Integer amount, Date date, String couponName, String couponDescription, Date couponStartDate, Date couponEndDate, int couponCategory, float price, String username, String companyName) {
        super(id, couponId, userId, amount, date);

        this.couponName = couponName;
        this.couponDescription = couponDescription;
        this.couponStartDate = couponStartDate;
        this.couponEndDate = couponEndDate;
        this.couponCategory = couponCategory;
        this.price = price;
        this.username = username;
        this.companyName = companyName;

    }

    public PurchaseExtended(Integer id, Integer couponId, Integer userId, Integer amount, Date date, String couponName, String couponDescription, Date couponStartDate, Date couponEndDate, int couponCategory, float price, String username, String companyName, String contactEmail) {
        super(id, couponId, userId, amount, date);

        this.couponName = couponName;
        this.couponDescription = couponDescription;
        this.couponStartDate = couponStartDate;
        this.couponEndDate = couponEndDate;
        this.couponCategory = couponCategory;
        this.price = price;
        this.username = username;
        this.companyName = companyName;

        this.contactEmail = contactEmail;
    }

    public PurchaseExtended(PurchaseEntity purchaseEntity){
        super(purchaseEntity);
        Coupon coupon = new Coupon(purchaseEntity.getCoupon());
        this.couponName = coupon.getName();
        this.couponDescription = coupon.getDescription();
        this.couponStartDate = coupon.getStartDate();
        this.couponEndDate = coupon.getEndDate();
        this.couponCategory = coupon.getCategoryId();
        this.price = coupon.getPrice();
        User user = new User(purchaseEntity.getUser());
        this.username = user.getUsername();
        Company company = new Company(purchaseEntity.getCoupon().getCompany());
        this.companyName = company.getName();

        this.contactEmail = company.getContactEmail();
    }

    public String getCouponName() {
        return couponName;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public Date getCouponStartDate() {
        return couponStartDate;
    }

    public Date getCouponEndDate() {
        return couponEndDate;
    }

    public int getCouponCategory() {
        return couponCategory;
    }

    public float getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }

    public String getCompanyName() {
        return companyName;
    }



    public String getContactEmail() {
        return contactEmail;
    }

    @Override
    public String toString() {
        return "PurchaseExtended{" +
                "couponName='" + couponName + '\'' +
                ", couponDescription='" + couponDescription + '\'' +
                ", couponStartDate=" + couponStartDate +
                ", couponEndDate=" + couponEndDate +
                ", couponCategory=" + couponCategory +
                ", price=" + price +
                ", username='" + username + '\'' +
                ", companyName='" + companyName + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", date=" + date +
                "} " + super.toString();
    }
}
