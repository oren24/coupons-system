package com.oren.coupons.dto;

import com.oren.coupons.entities.CategoryEntity;


public class Category {
	private Integer id;
	private String categoryName;

	public Category() {
	}


	public Category(CategoryEntity category) {
		this.id = category.getId();
		this.categoryName = category.getName();
	}

	public Category(String categoryName) {
		this.categoryName = categoryName;
	}

	public Category(int id, String categoryName) {
		this.id = id;
		this.categoryName = categoryName;
	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "Category{" +
				"categoryId=" + id +
				", categoryName='" + categoryName + '\'' +
				'}';
	}

}