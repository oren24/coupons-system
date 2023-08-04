package com.oren.coupons.dal;

import com.oren.coupons.dto.Company;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.entities.CouponEntity;
import com.oren.coupons.entities.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICompaniesDal extends CrudRepository<CompanyEntity,Long> {
    /**
     * get company by id
     * @param id - company id
     * @return company object with the given id
     *
     * */
    @Query("select c from CompanyEntity c where c.id = :id")
    Company getCompany(@Param("id") int id);

    @Query("select c from CompanyEntity c")
    List<Company> getAllCompanies();

/**
     * get company by name
     * @param name - company name
     * @return company object with the given name
     *
     * */
    @Query("select c from CompanyEntity c where c.name = :name")
    Company getCompanyByName(@Param("name") String name);

    /**
     * get company by registry number
     * @param registryNumber - company registry number
     * @return company object with the given registry number
     *
     * */
    @Query("select c from CompanyEntity c where c.registryNumber = :registryNumber")
    Company getCompanyByRegistryNumber(@Param("registryNumber") int registryNumber);
    /**
     * update company by id with the given parameters (except id) if the company exists in the database.
     * - if the company doesn't exist in the database, the method will do nothing.
     * - if the company exists in the database, the method will update the company with the given parameters.
     * - if the given parameter is null, the method will not update the field.
     *
     * @param name - the name of the company
     * @param registryNumber - the registry number of the company
     * @param address - the address of the company
     * @param contactEmail - the contact email of the company
     * @param id - the id of the company
     * */
    @Transactional
    @Modifying
    @Query("update CompanyEntity c set  c.name = :name, c.registryNumber = :registryNumber, c.address = :address, c.contactEmail = :contactEmail " +
            "where c.id = :id")
    void updateCompany( @Param("name") String name, @Param("registryNumber") Long registryNumber, @Param("address") String address, @Param("contactEmail") String contactEmail, @Param("id") int id);

    /**
     * delete company by id if the company exists in the database.
     * - if the company doesn't exist in the database, the method will do nothing.
     * - if the company exists in the database, the method will delete the company with the given id.
     *
     * @param id - the id of the company
     * */
    @Transactional
    @Modifying
    @Query("delete from CompanyEntity c where c.id = :id")
    void deleteCompany(@Param("id") int id);



   /**
    * check if company exists in the database by id
    * @param id - the id of the company
    * @return true if the company exists in the database, false if not
    * */
   @Query("select (count(c) > 0) from CompanyEntity c where c.id = :id")
   boolean isCompanyExist(@Param("id") int id);



}
