package com.i2btech.amazontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.NewPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amazonaws.regions.Regions;
import com.i2btech.amazontest.common.ApplicationConstants;
import com.i2btech.amazontest.common.AuthenticationConstants;
import com.i2btech.amazontest.helpers.ActivityDataFlowHelper;

public class MainActivity extends AppCompatActivity {

    private String username;
    private String password;

    private EditText usernameText;
    private EditText passwordText;

    private Button btnLogin;
    private Button btnRegister;

    private NewPasswordContinuation newPasswordContinuation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameText = (EditText) findViewById(R.id.txtUser);
        passwordText = (EditText) findViewById(R.id.txtPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnLoginRegister);

        // Initialize the required members to Work with AWS Incognito
        ActivityDataFlowHelper.initialize(getApplicationContext());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void login() {

        username = usernameText.getText().toString();
        password = passwordText.getText().toString();

        // Look for the user in the User Pool by its username and get a Session
        CognitoUser user = ActivityDataFlowHelper.getUserPool().getUser(username);
        user.getSessionInBackground(authHandler);
    }

    private void register() {
        Intent intent = new Intent(getApplicationContext(), RegistryActivity.class);
        startActivityForResult(intent, ApplicationConstants.REGISTER_ID);
    }

    private void getUserAuthentication(AuthenticationContinuation continuation, String username) {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails(username, this.password, null);
        continuation.setAuthenticationDetails(authenticationDetails);
        continuation.continueTask();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean passwordUpdated = false;

        if(requestCode == ApplicationConstants.PASSWORD_UPDATE_ID) {
            if(resultCode == RESULT_OK) {
                passwordUpdated = data.getBooleanExtra("PASSWORD_UPDATED", false);
            }
            if(passwordUpdated) {
                continueWithFirstTimeSignIn();
            }
        }

    }

    private void continueWithFirstTimeSignIn() {
        newPasswordContinuation.setPassword(ActivityDataFlowHelper.getNewPassword());

        try {
            newPasswordContinuation.continueTask();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Fallo. Causa: " + e.getCause(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        CognitoUser user = ActivityDataFlowHelper.getUserPool().getCurrentUser();
        username = user.getUserId();
        if(username != null) {
            user.getSessionInBackground(authHandler);
        }
    }

    AuthenticationHandler authHandler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            Intent intent = new Intent(getApplicationContext(), LoggedInActivity.class);
            startActivity(intent);
        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String UserId) {
            Toast.makeText(MainActivity.this, "Obteniendo información de usuario " + UserId, Toast.LENGTH_LONG).show();
            getUserAuthentication(authenticationContinuation, UserId);
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

        }

        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {
            String challenge = continuation.getChallengeName();

            switch (challenge) {
                case AuthenticationConstants.REQUIRES_NEW_PASSWORD:
                    newPasswordContinuation = (NewPasswordContinuation) continuation;
                    Intent intent = new Intent(getApplicationContext(), CompleteProfileActivity.class);
                    startActivityForResult(intent, ApplicationConstants.PASSWORD_UPDATE_ID);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(MainActivity.this, "Inicio de sesión fallido...", Toast.LENGTH_LONG).show();
        }
    };
}
