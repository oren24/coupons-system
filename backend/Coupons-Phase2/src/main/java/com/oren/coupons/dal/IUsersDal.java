package com.oren.coupons.dal;

import com.oren.coupons.dto.User;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.entities.UserEntity;
import com.oren.coupons.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IUsersDal extends JpaRepository<UserEntity, Long> {

	@Query("select u from UserEntity u where u.id = :id")
	User getUser(@Param("id") int id);

	@Query("select u from UserEntity u where u.username = :username")
	User getUserByUsername(@Param("username") String username);


	@Query("select u from UserEntity u")
	List<User> getAllUsers();


	@Query("select u from UserEntity u where u.company.id = :id")
	List<User> getAllUsersByCompanyId(@Param("id") int id);

	@Query("select u from UserEntity u where u.username = :username and u.password = :password")
	User getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	/**
	 * update user by id with the given parameters (except id) if the user exists in the database.
	 * - if the user doesn't exist in the database, the method will do nothing.
	 * - if the user exists in the database, the method will update the user with the given parameters.
	 * - if the given parameter is null, the method will not update the field.
	 *
	 * @param username - the username of the user
	 * @param password - the password of the user
	 * @param userType - the user type of the user
	 * @param company  - the company of the user
	 * @param id       - the id of the user
	 */
	@Transactional
	@Modifying
	@Query("update UserEntity u set u.username = :username, u.password = :password, u.userType = :userType, u.company = :company " +
			"where u.id = :id")
	void updateUser(@Param("username") String username, @Param("password") String password, @Param("userType") UserType userType, @Nullable @Param("company") CompanyEntity company, @Param("id") Integer id);

	@Transactional
	@Modifying
	@Query("delete from UserEntity u " +
			"where u.id = :id")
	void deleteUser(int id);

	@Query("select (count(u) > 0) from UserEntity u where u.id = :id")
	boolean isUserExists(@Param("id") Integer id);

	@Query("select (count(u) > 0) from UserEntity u where u.username like :username")
	boolean isUserExists(@Param("username") String username);

	@Query("select (count(u)>0) from UserEntity u where u.id = :id and u.company.id = :companyId")
	boolean isUserBelongToCompany(@Param("id") Integer id, @Param("companyId") Integer companyId);

	@Query("select (count(u) > 0) from UserEntity u where u.username = :username and u.password = :password")
	boolean userLogIn(@Param("username") String username, @Param("password") String password);


}
