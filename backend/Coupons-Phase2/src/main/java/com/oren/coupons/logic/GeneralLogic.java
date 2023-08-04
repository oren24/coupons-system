package com.oren.coupons.logic;

import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.exceptions.ApplicationException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.regex.Pattern;
@Service
public abstract class GeneralLogic {
     void validateEmailAddressStructure(String emailAddress) throws ApplicationException {
        String email = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(email);
        if (!pat.matcher(emailAddress).matches()) {
            throw new ApplicationException(ErrorType.EMAIL_FORMAT_INVALID);
        }
    }
    void validateStringLength(String string, int min, int max) throws ApplicationException{
         if (string.length()<min){
             throw new ApplicationException(ErrorType.USER_INPUT_TOO_SHORT);
         }
        if (string.length()>max){
            throw new ApplicationException(ErrorType.USER_INPUT_TOO_LONG);
        }
    }

    void validateDate(Date date) throws ApplicationException{
        
     }
}
