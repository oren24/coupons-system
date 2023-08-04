package com.oren.coupons.logic;

import com.oren.coupons.dal.IUsersDal;
import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.dto.User;
import com.oren.coupons.encryptions.HashFunction;
import com.oren.coupons.entities.CompanyEntity;
import com.oren.coupons.entities.UserEntity;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.utils.StatisticsUtils;
import com.oren.coupons.utils.JWTUtils;
import com.oren.coupons.encryptions.tokenConverters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserLogic extends GeneralLogic {


    private IUsersDal usersDal;
    private CompanyLogic companyLogic;
    @Autowired
    public UserLogic(IUsersDal usersDal , CompanyLogic companyLogic) {
        this.usersDal = usersDal;
        this.companyLogic = companyLogic;
    }
    public List<User> getAllUsersByCompanyId(int companyId) throws ApplicationException {
        return this.usersDal.getAllUsersByCompanyId(companyId);
    }

    public void addUser(User user) throws ApplicationException {
        // in order to not get the user type from the client
        // sets the default user type to customer - ADMIN can change it later
        user.setUserType(UserType.CUSTOMER);

        validateUser(user);
        String password = hashPassword(user.getPassword());
        user.setPassword(password);
        UserEntity userEntity = new UserEntity(user);
        StatisticsUtils.sendStatistics("User registration, user: "+user.getUsername());
        try {


            this.usersDal.save(userEntity);
        }catch (Exception e){
            throw new ApplicationException(ErrorType.USER_ALREADY_EXIST, "Failed to add user - user already exist", e);
        }
    }
    public String login(String userName, String password) throws ApplicationException {
        String hashedPassword = hashPassword(password);
        //todo: remove print before production
        System.out.println("\n\n\t\tpassword: "+password+"---> hashed password: "+hashedPassword+"\n\n");

        if( !this.usersDal.userLogIn(userName, hashedPassword)){
            throw new ApplicationException(ErrorType.GENERAL_ERROR,"user name and password does not match or exist");
        };
        try {
            String token = generateToken(userName, hashedPassword);
            // todo: remove print before production
            System.out.println("TOKEN = " + token+" for user: "+userName + " password: "+hashedPassword+" decoded token: "+token);
            return token;

        }catch (Exception e){
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Could not create token", e);
        }

    }
    private String generateToken(String userName, String password) throws Exception {

        User user = this.usersDal.getUserByUsernameAndPassword(userName, password);
        SuccessfulLoginDetails successfulLoginDetails = new SuccessfulLoginDetails(user);

        return JWTUtils.createJWT(successfulLoginDetails);
    }

    public User getUser(Integer id, String token) throws ApplicationException{
        // check if the user is authorized to get this user
        // if the user is a customer - he can only get himself
        // if the user is a company - he can only get users that belong to his company
        // if the user is an admin - he can get all users
        UserType tokenUserType = tokenConverters.getUserTypeFromToken(token);

        if (tokenUserType==UserType.CUSTOMER){
            id = tokenConverters.getUserIdFromToken(token);
            //return this.usersDal.getUser(id);
        }
        if (tokenUserType==UserType.COMPANY){
            Integer companyId = tokenConverters.getCompanyIdFromToken(token);
            if (!this.usersDal.isUserBelongToCompany(id, companyId)){
                throw new ApplicationException(ErrorType.UNAUTHORIZED, "You are not authorized to get this user");
            }
        }

        return this.usersDal.getUser(id);
    }

    public List<User> getAllUsers(String token) throws ApplicationException {
        UserType tokenUserType = tokenConverters.getUserTypeFromToken(token);
        if (tokenUserType==UserType.CUSTOMER){
            throw new ApplicationException(ErrorType.UNAUTHORIZED, "You are not authorized to get all users");
        }
        if (tokenUserType==UserType.COMPANY){
            Integer companyId = tokenConverters.getCompanyIdFromToken(token);
            return this.getAllUsersByCompanyId(companyId);
        }
        return this.usersDal.getAllUsers();
    }

    public void updateUser(User user, String token) throws ApplicationException{
        Integer userId = null;
        UserType tokenUserType = tokenConverters.getUserTypeFromToken(token);
        if (user.getId()!=null && tokenUserType==UserType.ADMIN){
            userId = user.getId();
        }else {
            userId = tokenConverters.getUserIdFromToken(token);
        }


        String userName = user.getUsername();
        validateUsername(userName);

        String password = user.getPassword();
        validatePassword(password);
        password = hashPassword(password);

        // if the user type is null - get the user type from the db
        UserType userType = user.getUserType();
        if (user.getUserType()!=null){
            validateUserType(user.getUserType());
        }else {
            User user1 = this.usersDal.getUser(userId);
            userType = user1.getUserType();
        }

        CompanyEntity company = new CompanyEntity();
        company.setId(user.getCompanyId());

        //Integer userId = user.getId();


         if (user.getCompanyId() == null){
            this.usersDal.updateUser(userName, password, userType,null, userId);
            return;}
        this.usersDal.updateUser(userName, password, userType, company, userId);
    }



    public void deleteUser(Integer id, String token) throws ApplicationException{
        UserType tokenUserType = tokenConverters.getUserTypeFromToken(token);
        if (tokenUserType!=UserType.ADMIN){
            throw new ApplicationException(ErrorType.UNAUTHORIZED, "You are not authorized to delete users");

        }
        this.usersDal.deleteUser(id);
    }

    private void validateUser(User user) throws ApplicationException {
        //System.out.println("during validation:\n\t"+user.toString());
        validateUsername(user.getUsername());
        validatePassword(user.getPassword());
        validateUserType(user.getUserType());
        validateCompanyId(user.getCompanyId());
    }

    private void validatePassword(String password) throws ApplicationException {
        validateStringLength(password, 8, 20);
    }
    private String hashPassword(String password) throws ApplicationException {

        try {
            return HashFunction.hash(password);
        }catch (Exception e){
            throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to hash password", e);
        }

    }
    private void validateCompanyId(Integer companyId) throws ApplicationException {
        //System.out.println("company id validation:\n\t"+companyId);
        if (companyId == null){
            return;
        }
        if (companyId < 1){
            return;
        }
        if (!companyLogic.isCompanyExist(companyId)){
            throw new ApplicationException(ErrorType.COMPANY_DOES_NOT_EXIST, "company does not exist");
        }
    }

    private void validateUserType(UserType type) throws ApplicationException{
        if (type == null){
            throw new ApplicationException(ErrorType.USER_TYPE_NULL);

        }
    }

    private void validateUsername(String username) throws ApplicationException {
        validateEmailAddressStructure(username);
    }
    boolean isUserExist(int id) throws ApplicationException {
        if (!this.usersDal.isUserExists(id)){
            throw new ApplicationException(ErrorType.USER_NOT_EXIST);
        }
        return true;
    }
    boolean isUserExist(String username) throws ApplicationException {
        if (!this.usersDal.isUserExists(username)){
            throw new ApplicationException(ErrorType.USER_NOT_EXIST);
        }
        return true;
    }


}
