package com.i2btech.firebasedemo.listeners;

import android.app.Activity;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.i2btech.firebasedemo.RegistryActivity;
import com.i2btech.firebasedemo.entities.User;

/**
 * Created by jflorez on 21-03-17.
 */

public class RegistrationListener implements View.OnClickListener {

    private FirebaseDatabase firebaseDatabase;

    private Activity activity;
    private TextView email;
    private TextView password;
    private TextView passwordConfirm;
    private TextView name;
    private TextView lastName;
    private TextView birthday;
    private FirebaseAuth firebaseAuth;

    private String errorMessage = "";


    public RegistrationListener(Activity activity, TextView email, TextView password, TextView passwordConfirm,
                                TextView name, TextView lastName, TextView birthday, FirebaseAuth firebaseAuth) {
        this.activity = activity;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.lastName = lastName;
        this.birthday = birthday;
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public void onClick(View v) {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();
        String name = this.name.getText().toString();
        String lastName = this.lastName.getText().toString();
        String birthday = this.birthday.getText().toString();

        if(isValidPassword(password, passwordConfirm) && email != null && !email.isEmpty()) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            User usuario = new User(name, lastName, birthday);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new SignupResultListener(activity, usuario));

        } else {
            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidPassword(String password, String passwordConfirm) {
        boolean isValidPassword = false;
        if(password == null || password.isEmpty() || passwordConfirm == null || passwordConfirm.isEmpty()) {
            errorMessage = "Contraseña inválida";
        } else if (password != null && !password.equals(passwordConfirm)) {
            errorMessage = "Las contraseñas no coinciden";
        }
        if(password != null && !password.isEmpty() && password.equals(passwordConfirm)) {
            isValidPassword = true;
        }

        return isValidPassword;
    }
}
