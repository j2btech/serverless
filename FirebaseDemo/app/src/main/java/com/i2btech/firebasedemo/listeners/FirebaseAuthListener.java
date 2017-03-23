package com.i2btech.firebasedemo.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.i2btech.firebasedemo.ProfileActivity;

/**
 * Created by jflorez on 21-03-17.
 */

public class FirebaseAuthListener implements FirebaseAuth.AuthStateListener {

    Activity activity;

    public FirebaseAuthListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
            Intent intent = new Intent(activity, ProfileActivity.class);
            activity.startActivity(intent);
        }
    }
}
