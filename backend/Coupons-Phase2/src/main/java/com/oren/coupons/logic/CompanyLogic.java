package com.oren.coupons.logic;

import com.oren.coupons.dal.ICompaniesDal;
import com.oren.coupons.dto.Company;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyLogic extends GeneralLogic {

    private ICompaniesDal companiesDal;

    @Autowired
    public CompanyLogic(ICompaniesDal companiesDal) {
        this.companiesDal = companiesDal;
    }

    public void addCompany(Company company) throws ApplicationException {
        validateCompany(company);
        CompanyEntity companyEntity = new CompanyEntity(company);
        this.companiesDal.save(companyEntity);
        StatisticsUtils.sendStatistics("Company registration, company: "+company.getName());
    }

    public Company getCompany(int companyId) throws ApplicationException {
        return this.companiesDal.getCompany(companyId);
    }

    public List<Company> getAllCompanies() throws ApplicationException {
        return this.companiesDal.getAllCompanies();
    }

    public void updateCompany(Company company) throws ApplicationException{
        validateCompany(company);
        String companyName = company.getName();
        Long companyRegistryNumber = company.getRegistryNumber();
        String companyAddress = company.getAddress();
        String companyContactEmail = company.getContactEmail();
        int companyId = company.getId();
        this.companiesDal.updateCompany(companyName, companyRegistryNumber, companyAddress, companyContactEmail, companyId);
    }

    public void deleteCompany(int id) throws ApplicationException{
        this.companiesDal.deleteCompany(id);
    }

    private void validateCompany(Company company) throws ApplicationException {
        validateCompanyName(company.getName());
        validateCompanyRegistryNumber(company.getRegistryNumber());
        validateCompanyAddress(company.getAddress());
        ValidateCompanyContactEmail(company.getContactEmail());
    }

    private void ValidateCompanyContactEmail(String email) throws ApplicationException {
        validateEmailAddressStructure(email);
    }

    private void validateCompanyAddress(String address) throws ApplicationException {
            validateStringLength(address, 3, 100);
    }

    private void validateCompanyName(String name) throws ApplicationException {
        validateStringLength(name, 3, 45);
    }


    private void validateCompanyRegistryNumber(Long regNum) throws ApplicationException{
        if (regNum > 519999999 || regNum < 510000000){
            throw new ApplicationException(ErrorType.COMPANY_REGISTRY_NUMBER_INVALID);
        }
    }

    public boolean isCompanyExist(Integer id) throws ApplicationException {
        return this.companiesDal.isCompanyExist(id);
    }
}
