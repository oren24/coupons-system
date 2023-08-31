package com.oren.coupons.dto;


import com.oren.coupons.entities.CouponEntity;

import java.sql.Date;

public class Coupon {
	private Integer id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private int categoryId;
	private String categoryName;
	private int amount;
	private float price;
	private int companyId;
	private String companyName;
	private String contactEmail;

	public Coupon() {

	}

	public Coupon(String name, String description, Date startDate, Date endDate, Integer categoryId, int amount, float price, int companyId) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
		this.amount = amount;
		this.price = price;
		this.companyId = companyId;
	}

	public Coupon(int id, String name, String description, Date startDate, Date endDate, Integer categoryId, int amount, float price, int companyId) {
		this(name, description, startDate, endDate, categoryId, amount, price, companyId);
		this.id = id;
	}

	public Coupon(CouponEntity couponEntity) {
		this.id = couponEntity.getId();
		this.name = couponEntity.getName();
		this.description = couponEntity.getDescription();
		this.startDate = couponEntity.getStartDate();
		this.endDate = couponEntity.getEndDate();

		Category category = new Category(couponEntity.getCategory());
		this.categoryId = category.getId();
		this.categoryName = category.getCategoryName();

		this.amount = couponEntity.getAmount();
		this.price = couponEntity.getPrice();

		Company company = new Company(couponEntity.getCompany());
		this.companyId = company.getId();
		this.companyName = company.getName();
		this.contactEmail = company.getContactEmail();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	@Override
	public String toString() {
		return "Coupon{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", categoryId=" + categoryId +
				", categoryName='" + categoryName + '\'' +
				", amount=" + amount +
				", price=" + price +
				", companyId=" + companyId +
				'}';
	}
}
