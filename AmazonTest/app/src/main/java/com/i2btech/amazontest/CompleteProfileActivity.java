package com.i2btech.amazontest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.i2btech.amazontest.helpers.ActivityDataFlowHelper;

public class CompleteProfileActivity extends AppCompatActivity {

    private Button btnUpdate;
    private EditText password;
    private EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        btnUpdate = (Button) findViewById(R.id.btnCompleteUpdate);
        password = (EditText) findViewById(R.id.txtCompletePassword);
        passwordConfirm = (EditText) findViewById(R.id.txtCompletePasswordConfirm);

        btnUpdate.setOnClickListener(btnListener);


    }

    @Override
    public void onBackPressed() {
        finishUpdate(false);
    }


    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String newPassword = password.getText().toString();
            String newPasswordConfirm = passwordConfirm.getText().toString();

            if(isValidPassword(newPassword, newPasswordConfirm)) {
                // Inicia flujo para la contraseña nueva
                ActivityDataFlowHelper.setNewPassword(newPassword);
                finishUpdate(true);
            }

        }
    };

    private boolean isValidPassword(String password, String passwordConfirm) {
        if(password != null && !password.isEmpty() && password.equals(passwordConfirm)) {
            return true;
        }
        return false;
    }

    private void finishUpdate(boolean wasPasswordUpdated) {
        Intent intent = new Intent();
        intent.putExtra("PASSWORD_UPDATED", wasPasswordUpdated);
        setResult(RESULT_OK, intent);
        finish();
    }
}
