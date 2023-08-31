package com.oren.coupons.entities;

import com.oren.coupons.dto.Purchase;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "purchases")
public class PurchaseEntity {
	@Id
	@GeneratedValue
	private Integer id;


	@Column(name = "amount", nullable = false)
	private Integer amount;
	@Column(name = "purchase_Date", nullable = false)
	private Date date;

	@ManyToOne
	private CouponEntity coupon;
	@ManyToOne
	private UserEntity user;

	public PurchaseEntity(Purchase purchase) {
		this.id = purchase.getId();
		this.amount = purchase.getAmount();
		this.date = purchase.getDate();
		this.coupon = new CouponEntity();
		this.coupon.setId(purchase.getCouponId());
		this.user = new UserEntity();
		this.user.setId(purchase.getUserId());
	}

	public PurchaseEntity() {

	}


	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public CouponEntity getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponEntity coupon) {
		this.coupon = coupon;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}


}
