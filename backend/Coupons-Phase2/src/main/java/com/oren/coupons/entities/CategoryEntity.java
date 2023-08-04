package com.oren.coupons.entities;

import com.oren.coupons.dto.Category;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "categories") // Table name in DB
public class CategoryEntity {
	@Id
	@GeneratedValue
	@Column(name = "id", nullable = false)
	private Integer id;
	@Column(name = "name",unique = true, nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "category")
	private List<CouponEntity> couponsList;

	public List<CouponEntity> getCouponsList() {
		return couponsList;
	}

	public void setCouponsList(List<CouponEntity> couponsList) {
		this.couponsList = couponsList;
	}

	public CategoryEntity() {
	}
	public CategoryEntity(Category category) {
		this.id = category.getId();
		this.name = category.getCategoryName();
	}
	public String getName() {
		return name;
	}

	public void setName(String categoryName) {
		this.name = categoryName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "CategoryEntity{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
	}
}