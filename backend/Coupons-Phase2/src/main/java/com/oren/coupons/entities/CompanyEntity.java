package com.oren.coupons.entities;

import com.oren.coupons.dto.Company;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="companies")
public class CompanyEntity {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CouponEntity> couponList;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserEntity> employeeList;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "registry_number", unique = true, nullable = false)
    private Long registryNumber;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "contact_Email", nullable = false)
    private String contactEmail;

    public CompanyEntity(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.registryNumber = company.getRegistryNumber();
        this.address = company.getAddress();
        this.contactEmail = company.getContactEmail();
    }
    public CompanyEntity() {

    }
    public CompanyEntity(Integer id, String name, Long registryNumber, String address, String contactEmail) {
        this.id = id;
        this.name = name;
        this.registryNumber = registryNumber;
        this.address = address;
        this.contactEmail = contactEmail;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CouponEntity> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponEntity> couponList) {
        this.couponList = couponList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegistryNumber(Long registryNumber) {
        this.registryNumber = registryNumber;
    }

    public List<UserEntity> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<UserEntity> employeeList) {
        this.employeeList = employeeList;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getRegistryNumber() {
        return registryNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return "CompanyEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", registryNumber=" + registryNumber +
                ", address='" + address + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }
}
