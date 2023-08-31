package com.oren.coupons.dal;

import com.oren.coupons.dto.Purchase;
import com.oren.coupons.dto.PurchaseExtended;
import com.oren.coupons.entities.CouponEntity;
import com.oren.coupons.entities.PurchaseEntity;
import com.oren.coupons.entities.UserEntity;
import com.oren.coupons.exceptions.ApplicationException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface IPurchasesDal extends CrudRepository<PurchaseEntity, Long> {
	@Query("select p from PurchaseEntity p where p.id = :id")
	Purchase getPurchase(@Param("id") Integer id) throws ApplicationException;

	@Query("select p from PurchaseEntity p")
	List<Purchase> getAllPurchases() throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.id = :id")
	PurchaseExtended getPurchaseExtendedById(Integer id) throws ApplicationException;

	@Query("select p from PurchaseEntity p ")
	List<PurchaseExtended> getPurchasesByUserId(Integer id) throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.id = :id")
	List<Purchase> getShortPurchasesByUserId(@Param("id") Integer id) throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.coupon.company.id = :id")
	List<Purchase> getShortPurchasesByCompanyId(@Param("id") Integer id) throws ApplicationException;

	@Query("select p from PurchaseEntity p ")
	List<PurchaseExtended> getAllPurchasesExtended() throws ApplicationException;
//    @Query("select new com.oren.coupons.dto.PurchaseExtended(p.id, c.id, u.id, p.amount, p.date,c.name,c.description,c.startDate,c.endDate,ca.name ,c.price, u.username, co.name,co.contactEmail) " +
//            "from PurchaseEntity p left outer join CouponEntity c on p.coupon.id = c.id  left outer join CategoryEntity ca on c.category.id=ca.id left outer join CompanyEntity co on c.company.id= co.id left outer join UserEntity u on p.user.id=u.id" )
//    List<PurchaseExtended> getAllPurchasesExtended2()throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.date between :dateStart and :dateEnd")
	List<PurchaseExtended> getPurchasesByPurchaseDateRange(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd) throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.coupon.startDate > :startDate or p.coupon.endDate < :endDate")
	List<PurchaseExtended> getPurchasesByDateAvailabilityRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

	List<PurchaseExtended> getByCoupon_Company_Id(Integer id);

	@Query("select p from PurchaseEntity p  where p.coupon.id = :couponId")
	List<PurchaseExtended> getPurchasesByCouponId(@Param("couponId") Integer couponId) throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.coupon.category.id = :category")
	List<PurchaseExtended> getPurchasesByCategory(@Param("category") String category) throws ApplicationException;

	@Query("select p from PurchaseEntity p where p.coupon.name like :couponName")
	List<PurchaseExtended> getPurchasesByCoupon(@Param("couponName") String couponName);

	@Query("select p from PurchaseEntity p where p.coupon.company.name like :name")
	List<PurchaseExtended> getPurchasesByCompany(@Param("name") String name);

	@Query("select p from PurchaseEntity p where p.user.username like :username")
	List<PurchaseExtended> getPurchasesByUser(@Param("username") String username);

	List<PurchaseExtended> findByCoupon_Company_NameLike(String name);

	@Modifying
	@Query("select p from PurchaseEntity p where p.coupon.company.id = :companyId")
	List<PurchaseExtended> getPurchasesByCompanyId(@Param("companyId") Integer companyId) throws ApplicationException;


	/**
	 * Updates a purchase in the database by id with the given parameters (except id) if the purchase exists in the database.
	 *
	 * @param amount - the amount of coupons purchased
	 * @param date   - the date of the purchase
	 * @param user   - the user who purchased the coupon
	 * @param coupon - the coupon purchased
	 * @param id     - the id of the purchase to update
	 */
	@Transactional
	@Modifying
	@Query("update PurchaseEntity p set p.amount = :amount, p.date = :date, p.user = :user, p.coupon = :coupon " +
			"where p.id = :id")
	void updatePurchase(@Param("amount") Integer amount, @Param("date") Date date, @Param("user") UserEntity user, @Param("coupon") CouponEntity coupon, @Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("delete from PurchaseEntity p where p.id = :id")
	void deletePurchase(Integer id);

	@Transactional
	@Modifying
	@Query("delete from PurchaseEntity p where p.coupon.id = :id")
	void deletePurchaseByCouponId(Integer id) throws ApplicationException;

	@Transactional
	@Modifying
	@Query("delete from PurchaseEntity p where p.user.id = :id")
	void deletePurchaseByUserId(Integer id) throws ApplicationException;


}

