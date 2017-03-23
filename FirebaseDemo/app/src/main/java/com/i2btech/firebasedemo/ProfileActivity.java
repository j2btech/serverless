package com.i2btech.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.i2btech.firebasedemo.entities.User;

public class ProfileActivity extends AppCompatActivity {

    private TextView uuid;
    private TextView name;
    private TextView birthday;
    private TextView serviceCalls;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uuid = (TextView) findViewById(R.id.txtUUID);
        name = (TextView) findViewById(R.id.txtName);
        birthday = (TextView) findViewById(R.id.txtBirthday);
        serviceCalls = (TextView) findViewById(R.id.txtServiceCalls);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        uuid.setText("UUID usuario: " + uid);

        database = FirebaseDatabase.getInstance().getReference(); // .child("users").child(uid);

        database.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    name.setText("Nombre: " + user.getName() + " " + user.getLastName());
                    birthday.setText("Cumpleaños: " + user.getBirthday());
                } else {
                    Toast.makeText(ProfileActivity.this, "No hay datos del usuario... Raro!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        database.child("tracking_data").child("callsToService").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalConnections = dataSnapshot.getValue().toString();
                serviceCalls.setText("Llamadas al servicio: " + totalConnections);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
