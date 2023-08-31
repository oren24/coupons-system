package com.oren.coupons.controllers;

import com.oren.coupons.dto.Purchase;
import com.oren.coupons.dto.PurchaseExtended;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.PurchaseLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;


@RestController
@RequestMapping("/purchase")
public class PurchasesController {

	private final PurchaseLogic purchaseLogic;

	@Autowired
	public PurchasesController(PurchaseLogic purchaseLogic) {
		this.purchaseLogic = purchaseLogic;
	}

	@PostMapping()
	public void createPurchase(@RequestHeader("Authorization") String token, @RequestBody Purchase purchase) throws ApplicationException {

		this.purchaseLogic.addPurchase(purchase, token);

	}

	@PutMapping
	public void updatePurchase(@RequestBody Purchase purchase) throws ApplicationException {
		this.purchaseLogic.updatePurchase(purchase);

	}

	@GetMapping()
	public List<Purchase> getAllPurchases(@RequestHeader("Authorization") String token) throws ApplicationException {
		return this.purchaseLogic.getAllPurchases(token);
	}

	@GetMapping("/byCategory")
	public List<PurchaseExtended> getPurchasesByCategory(@RequestParam("category") String category) throws ApplicationException {
		return this.purchaseLogic.getPurchasesByCategory(category);
	}

	//TODO: delete this method after testing
	@GetMapping("/byCouponId")
	public List<PurchaseExtended> getPurchasesByCoupon(@RequestParam("couponName") String couponName) throws ApplicationException {
		return this.purchaseLogic.getPurchasesByCoupon(couponName);
	}


	@GetMapping("/byPurchaseDateRange")
	public List<PurchaseExtended> getPurchasesByPurchaseDateRange(@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) throws ApplicationException {
		return this.purchaseLogic.getPurchasesByPurchaseDateRange(startDate, endDate);
	}

	@GetMapping("/byDateRange")
	public List<PurchaseExtended> getPurchasesByDateRange(@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) throws ApplicationException {
		return this.purchaseLogic.getPurchasesByDateRange(startDate, endDate);
	}


	@GetMapping("/{Id}")
	public Purchase getPurchase(@PathVariable("Id") int id) throws ApplicationException {
		return this.purchaseLogic.getPurchase(id);
	}

	@DeleteMapping("/{Id}")
	public void deletePurchase(@RequestHeader("Authorization") String token, @PathVariable("Id") int id) throws ApplicationException {
		this.purchaseLogic.deletePurchase(token, id);
	}

	@GetMapping("/extended")
	public List<PurchaseExtended> getAllPurchasesExtended(@RequestHeader("Authorization") String token) throws ApplicationException {
		return this.purchaseLogic.getAllPurchasesExtended(token);
	}

}
