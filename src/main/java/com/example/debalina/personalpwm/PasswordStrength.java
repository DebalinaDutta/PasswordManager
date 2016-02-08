package com.example.debalina.personalpwm;

/**
 * Created by Debalina on 2/7/2016.
 */
public class PasswordStrength {


    public PasswordStrength() {

    }

    public String CalPasswordStrength(String pss) {

        int length = 0, uppercase = 0, lowercase = 0, digits = 0, symbols = 0, score = 0;
        String strength = null;

        length = pss.length();
        if (length == 0) {
            strength = "none";
        }else {

            for (int i = 0; i < pss.length(); i++) {
                if (Character.isUpperCase(pss.charAt(i)))
                    uppercase++;
                else if (Character.isLowerCase(pss.charAt(i)))
                    lowercase++;
                else if (Character.isDigit(pss.charAt(i)))
                    digits++;

                symbols = length - uppercase - lowercase - digits;

                if ((uppercase > 0) && (lowercase > 0) && (digits > 0) && (symbols > 0)) {
                    strength = "valid";
                } else {
                    strength = "invalid";

                }

            }
        }

           return strength;
    }
}
