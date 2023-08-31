package com.oren.coupons.controllers;

import com.oren.coupons.dto.Company;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CompanyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompaniesController {

	private final CompanyLogic companyLogic;

	@Autowired
	public CompaniesController(CompanyLogic companyLogic) {
		this.companyLogic = companyLogic;
	}

	@PostMapping()
	public void createCompany(@RequestBody Company company) throws ApplicationException {
		this.companyLogic.addCompany(company);

	}

	@PutMapping
	public void updateCompany(@RequestBody Company company) throws ApplicationException {
		this.companyLogic.updateCompany(company);
	}

	@GetMapping()
	public List<Company> getAllCompanies() throws ApplicationException {
		return this.companyLogic.getAllCompanies();
	}

	@GetMapping("/{Id}")
	public Company getCompany(@PathVariable("Id") int id) throws ApplicationException {
		return this.companyLogic.getCompany(id);
	}


	@DeleteMapping("/{Id}")
	public void deleteCompany(@PathVariable("Id") int CompanyId) throws ApplicationException {
		this.companyLogic.deleteCompany(CompanyId);
	}


}
