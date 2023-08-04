package com.oren.coupons.controllers;

import com.oren.coupons.dto.Coupon;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CouponLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponsController {

    private CouponLogic couponLogic;

    @Autowired
    public CouponsController(CouponLogic couponLogic){
        this.couponLogic= couponLogic;
    }

    @PostMapping
    public void createCoupon(@RequestBody Coupon coupon) throws ApplicationException {
        this.couponLogic.addCoupon(coupon);
    }

    @PutMapping
    public void updateCoupon(@RequestBody Coupon coupon) throws ApplicationException{
        this.couponLogic.updateCoupon(coupon);
    }
    @GetMapping
    public List<Coupon> getAllCoupons() throws ApplicationException{
        List<Coupon> coupons = this.couponLogic.getAllCoupons();
        return coupons;
    }
    @GetMapping("/byCompanyId")
    public List<Coupon> getCouponsByCompanyId(@RequestParam("companyId") int id) throws ApplicationException {
        return this.couponLogic.getAllCouponsByCompanyId(id);
    }


    @GetMapping("/byEndDate")
    public List<Coupon> getCouponsByEndDate(@RequestParam("endDate") Date endDate) throws ApplicationException {
        return this.couponLogic.getAllCouponsByEndDate(endDate);
    }

    @GetMapping("/byStartDate")
    public List<Coupon> getCouponsByStartDate(@RequestParam("startDate") Date startDate) throws ApplicationException {
        return this.couponLogic.getAllCouponsByStartDate(startDate);
    }

    @GetMapping("/byMaxPrice")
    public List<Coupon> getCouponsByMaxPrice(@RequestParam("maxPrice") float maxPrice) throws ApplicationException {
        return this.couponLogic.getAllCouponsUpToPrice(maxPrice);
    }
    @GetMapping("/{id}")
    public Coupon getCoupon(@PathVariable("id") int id) throws ApplicationException {
        return this.couponLogic.getCoupon(id);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) throws ApplicationException{
        this.couponLogic.deleteCoupon(id);}
}
