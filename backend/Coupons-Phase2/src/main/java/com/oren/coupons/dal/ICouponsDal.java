package com.oren.coupons.dal;

import com.oren.coupons.dto.Coupon;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.entities.CouponEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface ICouponsDal extends CrudRepository<CouponEntity, Integer> {

	@Query("select c from CouponEntity c where c.id = :id")
	Coupon getCouponById(@Param("id") int id);

	@Query("select c from CouponEntity c")
	List<Coupon> getAllCoupons();

	@Query("select c from CouponEntity c")
	List<CouponEntity> getAllCouponsEntitys();

	@Query("select c from CouponEntity c where c.company.id = :id")
	List<Coupon> getAllCouponsByCompanyId(@Param("id") int id);

	@Query("select c from CouponEntity c where c.endDate <= :endDate")
	List<Coupon> getAllCouponsByEndDate(@Param("endDate") Date endDate);

	@Query("select c from CouponEntity c where c.startDate >= :startDate")
	List<Coupon> getAllCouponsByStartDate(@Param("startDate") java.sql.Date startDate);

	@Query("select c from CouponEntity c where c.price <= :maxPrice")
	List<Coupon> getAllCouponsUpToPrice(@Param("maxPrice") float maxPrice);

	@Query("select c from CouponEntity c where c.amount >= :amount")
	List<Coupon> getAllCouponsUpToAmount(@Param("amount") int amount);

	@Query("select c from CouponEntity c where c.category.id = :categoryId")
	List<Coupon> getAllCouponsByCategoryId(@Param("category") int categoryId);

	@Transactional
	@Modifying
	@Query("update CouponEntity c set c.name = null, c.description = :description where c.id = :id")
	int updateNameAndDescriptionById(@Param("description") String description, @Param("id") int id);

	@Transactional
	@Modifying
	@Query("update CouponEntity c set c.amount = :amount where c.id = :id")
	void updateAmountById(@Param("amount") int amount, @Param("id") int id);

	/**
	 * Updates the coupon with the given id.
	 * If a parameter is null, the field will not be updated.
	 *
	 * @param name        - can be null
	 * @param description - can be null
	 * @param startDate   - date format: yyyy-mm-dd - can be null
	 * @param endDate     - date format: yyyy-mm-dd - can be null
	 * @param category    - can be null
	 * @param amount      - can be null
	 * @param price       - can be null
	 * @param company     - can be null
	 * @param id          - the id of the coupon to update
	 */
	@Transactional
	@Modifying
	@Query("update CouponEntity c " +
			"set c.name = :name, c.description = :description, c.startDate = :startDate, c.endDate = :endDate, c.category = :category, c.amount = :amount, c.price = :price, c.company = :company " +
			"where c.id = :id")
	void updateCoupon(@Param("name") String name,
	                  @Param("description") String description,
	                  @Param("startDate") Date startDate,
	                  @Param("endDate") Date endDate,
	                  @Param("category") int category,
	                  @Param("amount") int amount,
	                  @Param("price") float price,
	                  @Param("company") CompanyEntity company,
	                  @Param("id") int id);

	@Transactional
	@Modifying
	@Query("delete from CouponEntity c where c.id = :id")
	void deleteCoupon(int id);


}

