package com.oren.coupons.entities;

import com.oren.coupons.dto.User;
import com.oren.coupons.enums.UserType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Users")
public class UserEntity {
	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "user_name", unique = true, nullable = false)
	private String username;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "user_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PurchaseEntity> PurchaseList;
	@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REMOVE, CascadeType.REFRESH})
	private CompanyEntity company;

	public UserEntity() {

	}

	public UserEntity(User user) {
		this.id = user.getId();

		this.username = user.getUsername();
		this.password = user.getPassword();
		this.userType = UserType.CUSTOMER;

		if (user.getCompanyId() != null) {

			this.company = new CompanyEntity();
			this.company.setId(user.getCompanyId());
		} else {
			this.company = null;
		}
	}

	public UserEntity(Integer id, String username, String password, UserType userType, Integer companyId) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.company = new CompanyEntity();
		this.company.setId(companyId);
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public List<PurchaseEntity> getPurchaseList() {
		return PurchaseList;
	}

	public void setPurchaseList(List<PurchaseEntity> purchsaeList) {
		this.PurchaseList = purchsaeList;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}


}
