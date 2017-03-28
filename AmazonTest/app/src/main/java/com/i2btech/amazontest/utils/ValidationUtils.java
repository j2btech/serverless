package com.i2btech.amazontest.utils;

import android.util.Log;

/**
 * Created by jflorez on 28-03-17.
 */

public class ValidationUtils {

    public static boolean arePasswordsValid(String password, String passwordConfirm) {
        if(password == null || passwordConfirm == null) {
            return false;
        }

        if(password != null && !password.isEmpty() && password.equals(passwordConfirm)) {
            return true;
        }

        return false;
    }

    public static String formatException(Exception exception) {
        String formattedString = "Internal Error";
        Log.getStackTraceString(exception);

        String temp = exception.getMessage();

        if(temp != null && temp.length() > 0) {
            formattedString = temp.split("\\(")[0];
            if(temp != null && temp.length() > 0) {
                return formattedString;
            }
        }

        return  formattedString;
    }
}
