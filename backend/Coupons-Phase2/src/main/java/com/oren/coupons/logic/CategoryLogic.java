package com.oren.coupons.logic;

import com.oren.coupons.dal.ICategoryDal;
import com.oren.coupons.dto.Category;
import com.oren.coupons.entities.CategoryEntity;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryLogic {
	private final ICategoryDal categoriesDal;

	@Autowired
	public CategoryLogic(ICategoryDal categoriesDal) {
		this.categoriesDal = categoriesDal;
	}

	public void addCategory(Category category) throws ApplicationException {
		validateCategory(category);
		CategoryEntity categoryEntity = new CategoryEntity(category);
		StatisticsUtils.sendStatistics("Category added, category: " + category.getId());
		categoriesDal.save(categoryEntity);
	}

	public Category getCategory(int categoryId) throws ApplicationException {
		Category category = categoriesDal.getById(categoryId);

		StatisticsUtils.sendStatistics("Category read, category: " + categoryId);
		return category;
	}

	public List<Category> getAllCategories() throws ApplicationException {
		return categoriesDal.getAll();
	}

	public void updateCategory(Category category) throws ApplicationException {
		validateCategory(category);

		categoriesDal.update(category.getCategoryName(), category.getId());
		StatisticsUtils.sendStatistics("Category updated, category: " + category.getId());
	}

	public void deleteCategory(int categoryId) throws ApplicationException {
		categoriesDal.deleteById(categoryId);
		StatisticsUtils.sendStatistics("Category removed, category: " + categoryId);
	}


	private void validateCategory(Category category) throws ApplicationException {
		validateCategoryName(category.getCategoryName());
	}

	private void validateCategoryName(String categoryName) throws ApplicationException {
		if (categoryName.length() > 45) {
			throw new ApplicationException(ErrorType.NAME_IS_TOO_LONG, "Category name is too long");
		}
		if (categoryName.length() < 2) {
			throw new ApplicationException(ErrorType.NAME_IS_TOO_SHORT, "Category name is too short");
		}
		if (categoriesDal.existByName(categoryName)) {
			throw new ApplicationException(ErrorType.NAME_IS_ALREADY_EXISTS, "Category name is already exists");
		}
	}


	boolean isCategoryExist(int categoryId) throws ApplicationException {
		return categoriesDal.existsById(categoryId);
	}

	public Category getCategoryByName(String categoryName) {
		return categoriesDal.getByName(categoryName);
	}
}

