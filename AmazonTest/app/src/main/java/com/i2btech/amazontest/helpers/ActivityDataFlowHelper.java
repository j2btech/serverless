package com.i2btech.amazontest.helpers;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jflorez on 27-03-17.
 */

public class ActivityDataFlowHelper {

    private static String newPassword;

    private static String username;

    private static Map<String, String> profileAttributes;

    private static CognitoUserPool userPool;

    private static final String POOL_ID = "ID DEL POOL";
    private static final String CLIENT_ID = "ID DEL CLIENTE";
    private static final String SECRET_ID = "ID SECRETO";

    public static void initialize(Context context) {
        profileAttributes = new HashMap<>();
        userPool = new CognitoUserPool(context, POOL_ID, CLIENT_ID, SECRET_ID, Regions.US_WEST_2);
    }

    public static String getNewPassword() {
        return newPassword;
    }

    public static void setNewPassword(String newPassword) {
        ActivityDataFlowHelper.newPassword = newPassword;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ActivityDataFlowHelper.username = username;
    }

    public static Map<String, String> getProfileAttributes() {
        return profileAttributes;
    }

    public static void setProfileAttributes(Map<String, String> profileAttributes) {
        ActivityDataFlowHelper.profileAttributes = profileAttributes;
    }

    public static CognitoUserPool getUserPool() {
        return userPool;
    }

    public static void setUserPool(CognitoUserPool userPool) {
        ActivityDataFlowHelper.userPool = userPool;
    }

    public static void addProfileAttribute(String attribute, String value) {
        if (profileAttributes == null) {
            profileAttributes = new HashMap<>();
        }

        profileAttributes.put(attribute, value);
    }
}
