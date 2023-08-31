package com.oren.coupons.dal;

import com.oren.coupons.dto.Category;
import com.oren.coupons.entities.CategoryEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoryDal extends CrudRepository<CategoryEntity, Integer> {

	@Query("select c from CategoryEntity c where c.id = :id")
	Category getById(@Param("id") Integer id);

	@Query("select c from CategoryEntity c")
	List<Category> getAll();

	@Query("select c from CategoryEntity c where c.name like :categoryName")
	Category getByName(@Param("categoryName") String categoryName);

	@Query("select (count(c) > 0) from CategoryEntity c where c.id = :id")
	boolean existById(@Param("id") Integer id);

	@Query("select (count(c) > 0) from CategoryEntity c where c.name like :categoryName")
	boolean existByName(@Param("categoryName") String categoryName);


	/**
	 * This method updates the category name by the given id.
	 *
	 * @param categoryName - the new category name.
	 * @param id           - the id of the category to update.
	 */
	@Transactional
	@Modifying
	@Query("update CategoryEntity c set c.name = :categoryName where c.id = :id")
	void update(@Param("categoryName") String categoryName, @Param("id") Integer id);

}