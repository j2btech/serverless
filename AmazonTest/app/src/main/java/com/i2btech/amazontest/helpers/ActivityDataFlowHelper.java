package com.i2btech.amazontest.helpers;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

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
    private static CognitoCachingCredentialsProvider credProvider;

    private static AmazonS3 s3client;
    private static TransferUtility transferUtility;

    private static final String POOL_ID = "USER POOL ID";
    private static final String IDENTITY_POOL_ID = "FEDERATED IDENTITY POOL ID";
    private static final String CLIENT_ID = "CLIENT ID";
    private static final String SECRET_ID = "SECRET ID";

    public static void initialize(Context context) {
        profileAttributes = new HashMap<>();
        userPool = new CognitoUserPool(context, POOL_ID, CLIENT_ID, SECRET_ID, Regions.US_WEST_2);

        credProvider = new CognitoCachingCredentialsProvider(context.getApplicationContext(),
                IDENTITY_POOL_ID, Regions.US_WEST_2);

        s3client = new AmazonS3Client(credProvider);
        transferUtility = new TransferUtility(s3client, context);
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

    public static AmazonS3 getS3client() {
        return s3client;
    }

    public static void setS3client(AmazonS3 s3client) {
        ActivityDataFlowHelper.s3client = s3client;
    }

    public static TransferUtility getTransferUtility() {
        return transferUtility;
    }

    public static void setTransferUtility(TransferUtility transferUtility) {
        ActivityDataFlowHelper.transferUtility = transferUtility;
    }

    public static CognitoCachingCredentialsProvider getCredProvider() {
        return credProvider;
    }

    public static void setCredProvider(CognitoCachingCredentialsProvider credProvider) {
        ActivityDataFlowHelper.credProvider = credProvider;
    }
}
