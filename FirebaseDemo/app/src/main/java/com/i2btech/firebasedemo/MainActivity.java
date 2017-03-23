package com.i2btech.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i2btech.firebasedemo.listeners.FirebaseAuthListener;
import com.i2btech.firebasedemo.listeners.LogingButtonListener;
import com.i2btech.firebasedemo.listeners.SignInResultListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private EditText emailText;
    private EditText passwordText;
    private Button loginButton;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.txtEmail);
        passwordText = (EditText) findViewById(R.id.txtPassword);
        loginButton = (Button) findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuthListener(this);
        register = (TextView) findViewById(R.id.txtRegister);

        // mDatabase = FirebaseDatabase.getInstance().getReference();

        loginButton.setOnClickListener(new LogingButtonListener(emailText, passwordText, this, firebaseAuth));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistryActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


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
