package com.oren.coupons.entities;

import com.oren.coupons.dto.Coupon;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="coupons")
public class CouponEntity {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PurchaseEntity> purchsaseList;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "start_Date", nullable = false)
    private Date startDate;
    @Column(name = "end_Date", nullable = false)
    private Date endDate;
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private CategoryEntity category;
    @Column(name = "amount", nullable = false)
    private int amount;
    @Column(name = "price", nullable = false)
    private float price;
    @ManyToOne
    @JoinColumn
    private CompanyEntity company;

    public List<PurchaseEntity> getPurchsaseList() {
        return purchsaseList;
    }

    public CouponEntity(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.description = coupon.getDescription();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.category =new CategoryEntity();
        this.category.setId(coupon.getCategoryId());
        this.amount = coupon.getAmount();
        this.price = coupon.getPrice();
        this.company = new CompanyEntity();
        this.company.setId(coupon.getCompanyId());
    }


    public CouponEntity() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPurchsaseList(List<PurchaseEntity> purchsaseList) {
        this.purchsaseList = purchsaseList;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "CouponEntity{" +
                "id=" + id +
                ",\n purchsaseList=" + purchsaseList +
                ",\n name='" + name + '\'' +
                ",\n description='" + description + '\'' +
                ",\n startDate=" + startDate +
                ",\n endDate=" + endDate +
                ",\n category=" + category +
                ",\n amount=" + amount +
                ",\n price=" + price +
                ",\n company=" + company.toString() +
                '}';
    }
}

