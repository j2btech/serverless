package com.i2btech.firebasedemo.listeners;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by jflorez on 21-03-17.
 */

public class SignInResultListener implements OnCompleteListener<AuthResult> {

    private Activity activity;

    public SignInResultListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()) {
            Toast.makeText(activity, "Sesión iniciada", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, "Datos de sesión no válidos", Toast.LENGTH_LONG).show();
        }
    }
}
