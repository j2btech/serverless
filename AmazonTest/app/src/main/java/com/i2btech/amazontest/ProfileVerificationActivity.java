package com.i2btech.amazontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.i2btech.amazontest.helpers.ActivityDataFlowHelper;
import com.i2btech.amazontest.utils.ValidationUtils;

public class ProfileVerificationActivity extends AppCompatActivity {

    private TextView verificationCodeText;
    private TextView usernameText;
    private Button verifyButton;
    private String username;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_verification);

        usernameText = (TextView) findViewById(R.id.txtVerifyUsername);

        verificationCodeText = (TextView) findViewById(R.id.txtVerifyCode);
        verifyButton = (Button) findViewById(R.id.btnVerifyVerify);

        extras = getIntent().getExtras();
        username = extras.getString("username");

        if(username != null) {
            usernameText.setVisibility(View.INVISIBLE);
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishVerification(false);
    }

    private void verify() {
        String verificationCode = verificationCodeText.getText().toString();
        String username = this.username == null ? usernameText.getText().toString() : this.username;

        if(verificationCode == null || verificationCode.isEmpty()) {
            Toast.makeText(ProfileVerificationActivity.this, "Código no ingresado", Toast.LENGTH_LONG).show();
        } else {
            ActivityDataFlowHelper.getUserPool().getUser(username).confirmSignUpInBackground(verificationCode, true, verifyHandler);
        }
    }



    GenericHandler verifyHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            finishVerification(true);
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(ProfileVerificationActivity.this, "Verificación fallida.\n" + ValidationUtils.formatException(exception), Toast.LENGTH_LONG).show();
        }
    };

    private void finishVerification(boolean success) {
        Intent intent = new Intent();
        intent.putExtra("VERIFICATION_SUCCESS", success);
        setResult(RESULT_OK);
        finish();
    }
}
