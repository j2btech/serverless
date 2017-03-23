package com.i2btech.firebasedemo.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.i2btech.firebasedemo.entities.User;

/**
 * Created by jflorez on 21-03-17.
 */

public class SignupResultListener implements OnCompleteListener<AuthResult> {

    private Activity activity;
    private DatabaseReference database;
    private User user;

    public  SignupResultListener(Activity activity, User user) {
        this.activity = activity;
        this.database = FirebaseDatabase.getInstance().getReference();
        this.user = user;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()) {

            String uuid = task.getResult().getUser().getUid();
            // Toast.makeText(activity, "UUID: " + uuid + "User: " + this.user.getName(), Toast.LENGTH_LONG).show();

            User usuario = new User(user.getName(), user.getLastName(), user.getBirthday());
            database.child("users").child(uuid).setValue(usuario);

        } else {
            Toast.makeText(activity, "Registro fallido. Verifique el correo ingresado.", Toast.LENGTH_LONG).show();
        }
    }
}
