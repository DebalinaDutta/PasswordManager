package com.example.debalina.personalpwm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Debalina on 1/1/2016.
 * Password validation using regex or regular expression
 */
public class validatePassword {

    public boolean validatePWD(String passWord, int passwordType) {

        Boolean PasswordIsValid = false;

        //if password is numeric and 6 byte long - type 1
        if (passwordType == 1) {

            NumPassVal numpassVal = new NumPassVal();
            if (numpassVal.isValid(passWord)) {
                PasswordIsValid = true;
            }
        }
        return PasswordIsValid;
    }


    public class NumPassVal {

        Pattern p = Pattern.compile("\\d{6}");

        private boolean isValid(String passwd) {
            Matcher m = p.matcher(passwd);
            return m.matches();
        }
    }
}
