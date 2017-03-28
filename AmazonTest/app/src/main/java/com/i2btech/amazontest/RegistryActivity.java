package com.i2btech.amazontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.i2btech.amazontest.common.ApplicationConstants;
import com.i2btech.amazontest.helpers.ActivityDataFlowHelper;
import com.i2btech.amazontest.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

public class RegistryActivity extends AppCompatActivity {

    private EditText usernameText;
    private EditText passwordText;
    private EditText passwordConfirmText;
    private EditText emailText;
    private EditText nameText;
    private EditText lastnameText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        // Get the reference of the GUI components
        usernameText = (EditText) findViewById(R.id.txtRegUsername);
        passwordText = (EditText) findViewById(R.id.txtRegPassword);
        passwordConfirmText = (EditText) findViewById(R.id.txtRegPasswordConfirm);
        emailText = (EditText) findViewById(R.id.txtRegEmail);
        nameText = (EditText) findViewById(R.id.txtRegName);
        lastnameText = (EditText) findViewById(R.id.txtRegLastName);
        registerButton = (Button) findViewById(R.id.btnRegRegister);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConfirm = passwordConfirmText.getText().toString();
        String email = emailText.getText().toString();
        String name = nameText.getText().toString();
        String lastName = lastnameText.getText().toString();

        // Validate password
        if(ValidationUtils.arePasswordsValid(password, passwordConfirm)) {
            CognitoUserAttributes userAttributes = new CognitoUserAttributes();
            userAttributes.addAttribute("email", email);
            userAttributes.addAttribute("given_name", name);
            userAttributes.addAttribute("family_name", lastName);

            Toast.makeText(RegistryActivity.this, "Intentando registrar a " + name + " " + lastName, Toast.LENGTH_LONG).show();

            ActivityDataFlowHelper.getUserPool().signUpInBackground(username, password, userAttributes, null, signUpHandler);
        } else {
            Toast.makeText(RegistryActivity.this, "Contraseña errada", Toast.LENGTH_LONG).show();
        }
    }


    SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Verifica si el estado del usuario es confirmado o no
            if(signUpConfirmationState) {
                finish(true);
            } else {
                Intent intent = new Intent(getApplicationContext(), ProfileVerificationActivity.class);
                intent.putExtra("username", user.getUserId());
                startActivityForResult(intent, ApplicationConstants.PROFILE_VERIFICATION_ID);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(RegistryActivity.this, "Error al registrar.\n" + ValidationUtils.formatException(exception), Toast.LENGTH_LONG).show();
        }
    };

    private void finish(boolean success) {
        Intent intent = new Intent();
        intent.putExtra("REGISTRY_SUCCESS", success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ApplicationConstants.PROFILE_VERIFICATION_ID) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this, "Activado correctamente!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Activación pendiente", Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }
}
