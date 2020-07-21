package com.example.fatchimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class profile extends AppCompatActivity {

    private TextView name, mobilenumber, helloname, profileLogout;
    private DatabaseHandler dbcart;
    private HashMap<String, String> mapp;
    private static final String TAG = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        mapp = dbcart.getuser_info();
        Log.d(TAG, "onCreateee: " + mapp.size());

        helloname.setText(mapp.get("username"));
        name.setText(mapp.get("username"));
        mobilenumber.setText(mapp.get("userMobile"));

        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbcart.deleteuserdata();
                dbcart.clearallcartdata();
                Intent intent = new Intent(profile.this,sign_in.class);
                startActivity(intent);
            }
        });


    }

    private void init() {
        name = findViewById(R.id.profilename);
        helloname = findViewById(R.id.helloname);
        profileLogout = findViewById(R.id.profileLogout);
        mobilenumber = findViewById(R.id.profilenumber);
        dbcart = new DatabaseHandler(profile.this);
        mapp = new HashMap<>();
    }
}
