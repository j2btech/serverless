package com.i2btech.firebasedemo.listeners;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.i2btech.firebasedemo.MainActivity;

/**
 * Created by jflorez on 21-03-17.
 */

public class LogingButtonListener implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Activity activity;
    private FirebaseAuth firebaseAuth;

    public LogingButtonListener(EditText email, EditText password, Activity activity, FirebaseAuth firebaseAuth) {
        this.email = email;
        this.password = password;
        this.activity = activity;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onClick(View v) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(isValidValue(email) && isValidValue(password)) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new SignInResultListener(activity));
        }
    }

    private void login(String email, String password) {

    }

    private boolean isValidValue(String text){
        return text != null && !text.isEmpty();
    }
}
