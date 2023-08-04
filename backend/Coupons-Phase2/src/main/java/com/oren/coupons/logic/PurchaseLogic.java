package com.oren.coupons.logic;

import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.dal.IPurchasesDal;
import com.oren.coupons.dal.IUsersDal;
import com.oren.coupons.dto.Purchase;
import com.oren.coupons.dto.PurchaseExtended;
import com.oren.coupons.entities.CouponEntity;
import com.oren.coupons.entities.PurchaseEntity;
import com.oren.coupons.entities.UserEntity;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class PurchaseLogic extends GeneralLogic{

    private IPurchasesDal purchasesDal;
    private CouponLogic couponLogic;
    private UserLogic userLogic;
    private final IUsersDal iUsersDal;

    @Autowired
    public PurchaseLogic(IPurchasesDal purchasesDal, CouponLogic couponLogic, UserLogic userLogic,
                         IUsersDal iUsersDal){
        this.purchasesDal = purchasesDal;
        this.couponLogic =  couponLogic;
        this.userLogic = userLogic;
        this.iUsersDal = iUsersDal;
    }

    public void addPurchase(Purchase purchase,String token) throws ApplicationException {
        try {
            SuccessfulLoginDetails successfulLoginDetails = JWTUtils.decodeJWT(token);
            purchase.setUserId(successfulLoginDetails.getId());
            Date currentDate = Date.valueOf(LocalDate.now());
            purchase.setDate(currentDate);
        } catch (Exception e) {
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't confirm purchase",e);
        }
        validatePurchase(purchase);
        PurchaseEntity purchaseEntity = new PurchaseEntity(purchase);
        couponLogic.updateAmountById(purchase.getAmount(),purchase.getCouponId());
        this.purchasesDal.save(purchaseEntity);
    }

    public Purchase getPurchase(Integer id) throws ApplicationException {

        return this.purchasesDal.getPurchase(id);
    }
    public PurchaseExtended getPurchaseToClient(Integer id) throws ApplicationException {
        return this.purchasesDal.getPurchaseExtendedById(id);
    }
    public List<PurchaseExtended> getPurchasesByDateRange(Date startDate, Date endDate) throws ApplicationException {
        return this.purchasesDal.getPurchasesByDateAvailabilityRange(startDate, endDate);
    }
    public List<PurchaseExtended> getPurchasesByPurchaseDateRange(Date startDate, Date endDate) throws ApplicationException {
        return this.purchasesDal.getPurchasesByPurchaseDateRange(startDate, endDate);
    }/*
    public List<PurchaseExtended> getPurchasesByUserId() throws ApplicationException {
        System.out.println("in get purchases by user id");

        List<PurchaseEntity>  purchaseEntityList =this.purchasesDal.getPurchaseEntityByUserId();

        return this.purchasesDal.getPurchasesByUserId();
    }*/
    public List<PurchaseExtended> getPurchasesByCategory(String category) throws ApplicationException {
        return this.purchasesDal.getPurchasesByCategory(category);
    }/*
    public List<PurchaseExtended> getPurchasesByCompany(String company) throws ApplicationException {
        return this.purchasesDal.getPurchasesByCompany(company);
    }*/
    public List<PurchaseExtended> getPurchasesByCompanyId(Integer companyId) throws ApplicationException {
        return this.purchasesDal.getByCoupon_Company_Id(companyId);//this.purchasesDal.getPurchasesByCompanyId(companyId);
    }
    public List<Purchase> getAllPurchases(String token) throws ApplicationException {
        List<Purchase> purchases = null;
        SuccessfulLoginDetails successfulLoginDetails;
        try {
            successfulLoginDetails = JWTUtils.decodeJWT(token);
        } catch (Exception e) {
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't confirm purchase", e);
        }
        UserType userType = successfulLoginDetails.getUserType();
        if (userType == UserType.ADMIN) {
            purchases =this.purchasesDal.getAllPurchases();
        }
        if (userType == UserType.CUSTOMER) {
            purchases= this.purchasesDal.getShortPurchasesByUserId(successfulLoginDetails.getId());
        }
        if (userType == UserType.COMPANY) {
            purchases = this.purchasesDal.getShortPurchasesByCompanyId(successfulLoginDetails.getCompanyId());
        }
        return purchases;
    }

    public void updatePurchase(Purchase purchase) throws ApplicationException {
        validatePurchase(purchase);
        Integer purchaseId = purchase.getId();
        Integer couponId = purchase.getCouponId();
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(couponId);
        Integer userId = purchase.getUserId();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        Date date = purchase.getDate();
        Integer amount = purchase.getAmount();
        this.purchasesDal.updatePurchase( purchaseId, date, userEntity,couponEntity,  amount);
    }

    public void deletePurchase(String token,Integer id) throws ApplicationException {
        SuccessfulLoginDetails successfulLoginDetails;
        try {
            successfulLoginDetails = JWTUtils.decodeJWT(token);
        } catch (Exception e) {
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't confirm purchase", e);
        }
        UserType userType = successfulLoginDetails.getUserType();
        if (userType == UserType.ADMIN) {
            this.purchasesDal.deletePurchase(id);
        }
        Purchase purchase = this.purchasesDal.getPurchase(id);
        Integer userId = purchase.getUserId();
        if (userType == UserType.CUSTOMER && userId.equals(successfulLoginDetails.getId())) {
            this.purchasesDal.deletePurchase(id);
        }


    }
    public List<PurchaseExtended> getAllPurchasesExtended(String token) throws ApplicationException {

        List<PurchaseExtended> purchases = null;
        SuccessfulLoginDetails successfulLoginDetails;
        try {
            successfulLoginDetails = JWTUtils.decodeJWT(token);
        } catch (Exception e) {
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Couldn't confirm purchase", e);
        }
        UserType userType = successfulLoginDetails.getUserType();
        if (userType == UserType.ADMIN) {
            purchases =this.purchasesDal.getAllPurchasesExtended();
        }
        if (userType == UserType.CUSTOMER) {
            purchases= this.purchasesDal.getPurchasesByUserId(successfulLoginDetails.getId());
        }
        if (userType == UserType.COMPANY) {
            purchases = this.purchasesDal.getPurchasesByCompanyId(successfulLoginDetails.getCompanyId());
        }

        return purchases;
    }


    private void validatePurchase(Purchase purchase) throws ApplicationException {
        validateCouponId(purchase.getCouponId());
        validateUserId(purchase.getUserId());
        validateAmount(purchase);
        validatePurchaseDate(purchase.getDate());
    }

    private void validatePurchaseDate(Date date) throws ApplicationException {
        validateDate(date);
    }

    private void validateAmount(Purchase purchase) throws ApplicationException {
        if (purchase.getAmount()<1){
            throw new ApplicationException(ErrorType.PURCHASE_AMOUNT_NOT_VALID);
        }
        else if(purchase.getAmount() > couponLogic.getCoupon(purchase.getCouponId()).getAmount()){
            throw new ApplicationException(ErrorType.PURCHASE_AMOUNT_NOT_VALID, "The amount of coupons you want to purchase is bigger than the amount of coupons available");
        }
    }

    private void validateUserId(Integer userId) throws ApplicationException {

        if (!userLogic.isUserExist(userId)){
            throw new ApplicationException(ErrorType.USER_NOT_EXIST);
        }
    }

    private void validateCouponId(Integer couponId) throws ApplicationException {
        if (!couponLogic.isCouponExist(couponId)){
            throw new ApplicationException(ErrorType.COUPON_DOES_NOT_EXIST);
        }
    }

    public List<PurchaseExtended> getPurchasesByCoupon(String couponName) {
        return this.purchasesDal.getPurchasesByCoupon(couponName);
    }
}
