package com.i2btech.firebasedemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.i2btech.firebasedemo.listeners.FirebaseAuthListener;
import com.i2btech.firebasedemo.listeners.LogingButtonListener;
import com.i2btech.firebasedemo.listeners.RegistrationListener;

public class RegistryActivity extends AppCompatActivity {

    private TextView email;
    private TextView password;
    private TextView passwordConfirm;
    private TextView name;
    private TextView lastName;
    private TextView birthday;
    private Button btnRegister;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPassword);
        passwordConfirm = (EditText) findViewById(R.id.txtPasswordConfirm);
        name = (EditText) findViewById(R.id.txtNombre);
        lastName = (EditText) findViewById(R.id.txtApellido);
        birthday = (EditText) findViewById(R.id.txtBornDate);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuthListener(this);


        btnRegister.setOnClickListener(new RegistrationListener(this, email, password, passwordConfirm, name, lastName, birthday, firebaseAuth));
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

}
