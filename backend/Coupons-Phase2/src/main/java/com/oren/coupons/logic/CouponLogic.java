package com.oren.coupons.logic;

import com.oren.coupons.dal.ICouponsDal;

import com.oren.coupons.dto.Coupon;
import com.oren.coupons.dto.Purchase;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.entities.CouponEntity;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.exceptions.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.util.List;

@Service
public class CouponLogic extends GeneralLogic{

    private ICouponsDal couponsDal;
    private CompanyLogic companyLogic;
    @Autowired
    public CouponLogic(ICouponsDal couponsDal, CompanyLogic companyLogic) {
        this.couponsDal = couponsDal;
        this.companyLogic = companyLogic;
    }

    public void addCoupon(Coupon coupon) throws ApplicationException {
        validateCoupon(coupon);
        CouponEntity couponEntity = new CouponEntity(coupon);
        this.couponsDal.save(couponEntity);
    }


    public Coupon getCoupon(int id) throws ApplicationException {
        return this.couponsDal.getCouponById(id);
    }

    public List<Coupon> getAllCoupons() throws ApplicationException{


        List<Coupon> coupons = this.couponsDal.getAllCoupons();

        return coupons;
    }

    public List<Coupon> getAllCouponsUpToPrice(float maxPrice) throws ApplicationException{
        return this.couponsDal.getAllCouponsUpToPrice(maxPrice);
    }
    public List<Coupon> getAllCouponsByCompanyId(int companyId) throws ApplicationException{
        return this.couponsDal.getAllCouponsByCompanyId(companyId);
    }
    public List<Coupon> getAllCouponsByEndDate(Date endDate) throws ApplicationException{
        return this.couponsDal.getAllCouponsByEndDate(endDate);
    }
    public List<Coupon> getAllCouponsByStartDate(Date startDate) throws ApplicationException{
        return this.couponsDal.getAllCouponsByStartDate(startDate);
    }
    public void updateCoupon(Coupon coupon) throws ApplicationException{
        validateCoupon(coupon);
        String couponName = coupon.getName();
        String couponDescription = coupon.getDescription();
        Date couponStartDate = coupon.getStartDate();
        Date couponEndDate = coupon.getEndDate();
        int couponAmount = coupon.getAmount();
        float couponPrice = coupon.getPrice();
        int couponCompanyId = coupon.getCompanyId();
        CompanyEntity  companyEntity = new CompanyEntity();
        companyEntity.setId(couponCompanyId);
        int couponCategoryId = coupon.getCategoryId();
        int couponId = coupon.getId();
        this.couponsDal.updateCoupon(couponName, couponDescription, couponStartDate, couponEndDate,couponCategoryId, couponAmount, couponPrice, companyEntity,  couponId);

    }

    public void updateAmountById(int amount,int id) throws ApplicationException{
        Coupon coupon = this.couponsDal.getCouponById(id);
        if (coupon.getAmount()<amount){
            throw new ApplicationException(ErrorType.COUPON_AMOUNT_INVALID,"amount is bigger than the amount of the coupon");
        }
        int newAmount = coupon.getAmount()-amount;
        this.couponsDal.updateAmountById(newAmount,coupon.getId());
    }
    public void deleteCoupon(int id) throws ApplicationException{
        this.couponsDal.deleteCoupon(id);
    }

    private void validateCoupon(Coupon coupon) throws ApplicationException {
        validateCouponName(coupon.getName());
        validateCouponDescription(coupon.getDescription());
        validateCouponStartDate(coupon.getStartDate());
        validateCouponEndDate(coupon.getEndDate());
        validateCouponCategory(coupon.getCategoryId());
        validateCouponAmount(coupon.getAmount());
        validateCouponPrice(coupon.getPrice());
        validateCompanyId(coupon.getCompanyId());
    }

    private void validateCompanyId(int companyId) throws ApplicationException {

        companyLogic.isCompanyExist(companyId);
    }

    private void validateCouponPrice(float price) throws ApplicationException {
        if (price<0){
            throw new ApplicationException(ErrorType.COUPON_PRICE_INVALID);
        }
    }

    private void validateCouponAmount(int amount) throws ApplicationException {
        if (amount<0){
            throw new ApplicationException(ErrorType.COUPON_AMOUNT_INVALID);
        }
    }

    private void validateCouponCategory(int category) throws ApplicationException {
        if (category<0){
            throw new ApplicationException(ErrorType.COUPON_CATEGORY_INVALID);
        }
    }

    private void validateCouponEndDate(Date endDate) throws ApplicationException {
        validateDate(endDate);
    }

    private void validateCouponStartDate(Date startDate) throws ApplicationException {
        validateDate(startDate);
    }

    private void validateCouponDescription(String description) throws ApplicationException {
        validateStringLength(description, 10, 100);
    }

    private void validateCouponName(String name) throws ApplicationException {
        validateStringLength(name, 4, 15);
    }

     public boolean isCouponExist(int id) throws ApplicationException {
        return  this.couponsDal.existsById(id);
    }
}
