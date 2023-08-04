package com.oren.coupons.dto;

import com.oren.coupons.entities.CompanyEntity;

public class Company{
    private int id;
    private String name;
    private Long registryNumber;
    //todo could be a long here, no?
    private String address;
    private String contactEmail;
    public Company() {

    }

    public Company(String name, Long registryNumber, String address, String contactEmail) {
        this.name = name;
        this.registryNumber = registryNumber;
        this.address = address;
        this.contactEmail = contactEmail;
    }

    public Company(int id, String name, Long registryNumber, String address, String contactEmail) {
        this(name, registryNumber, address, contactEmail);
        this.id = id;
    }

    public Company(CompanyEntity company) {
        this.id = company.getId();
        this.name = company.getName();
        this.registryNumber = company.getRegistryNumber();
        this.address = company.getAddress();
        this.contactEmail = company.getContactEmail();
    }

    public int getId() {
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
        return "Company{" +
                "companyId=" + id +
                ", name='" + name + '\'' +
                ", registryNumber=" + registryNumber +
                ", address='" + address + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                '}';
    }
}
