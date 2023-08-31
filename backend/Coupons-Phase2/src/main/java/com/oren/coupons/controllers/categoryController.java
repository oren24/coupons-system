package com.oren.coupons.controllers;

import com.oren.coupons.dto.Category;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CategoryLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class categoryController {
	private final CategoryLogic categoryLogic;

	@Autowired
	public categoryController(CategoryLogic categoryLogic) {
		this.categoryLogic = categoryLogic;
	}

	@PostMapping
	public void addCategory(@RequestBody Category category) throws ApplicationException {
		categoryLogic.addCategory(category);
	}

	@PutMapping
	public void updateCategory(@RequestBody Category category) throws ApplicationException {
		categoryLogic.updateCategory(category);
	}

	@GetMapping
	public List<Category> getAllCategories() throws ApplicationException {
		return categoryLogic.getAllCategories();
	}

	@GetMapping("/{categoryId}")
	public Category getCategory(@PathVariable("categoryId") int categoryId) throws ApplicationException {
		return categoryLogic.getCategory(categoryId);
	}

	@GetMapping("/byName")
	public Category getCategoryByName(@Param("categoryName") String categoryName) throws ApplicationException {
		return categoryLogic.getCategoryByName(categoryName);
	}

	@DeleteMapping("/{categoryId}")
	public void deleteCategory(@PathVariable("categoryId") int categoryId) throws ApplicationException {
		categoryLogic.deleteCategory(categoryId);
	}
}
