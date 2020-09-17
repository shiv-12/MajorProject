package com.example.fatchimage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class profile extends AppCompatActivity {

    private TextView name, mobilenumber, helloname, profileLogout, profileaddress, feedback, feedbacksubmit;
    private DatabaseHandler dbcart;
    private HashMap<String, String> mapp;
    private EditText feedbackedit;
    private static final String TAG = "profile";
    private Dialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

//        myRef.setValue("helloworld");
        mapp = dbcart.getuser_info();
        Log.d(TAG, "onCreateee: " + mapp);

        helloname.setText(mapp.get("username"));
        name.setText(mapp.get("username"));
        mobilenumber.setText(mapp.get("userMobile"));
        profileaddress.setText(mapp.get("userAddress"));

        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbcart.deleteuserdata();
                dbcart.clearallcartdata();
                Intent intent = new Intent(profile.this, sign_in.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.feedback);
                feedbackedit = dialog.findViewById(R.id.feedbackedit);
                feedbacksubmit = dialog.findViewById(R.id.feedbacksubmit);
                dialog.getWindow().setBackgroundDrawableResource(R.color.blacktrans);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                feedbacksubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String pushid = myRef.push().getKey();
                        myRef.child("feedback").child(pushid).child("name").setValue(mapp.get("username"));
                        myRef.child("feedback").child(pushid).child("number").setValue(mapp.get("userMobile"));
                        myRef.child("feedback").child(pushid).child("feedback").setValue(feedbackedit.getText().toString());
                        Toast.makeText(profile.this, "feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }

    private void init() {
        name = findViewById(R.id.profilename);
        helloname = findViewById(R.id.helloname);
        profileLogout = findViewById(R.id.profileLogout);
        mobilenumber = findViewById(R.id.profilenumber);
        profileaddress = findViewById(R.id.profileaddress);
        feedback = findViewById(R.id.feedback);
        dbcart = new DatabaseHandler(profile.this);
        mapp = new HashMap<>();
        dialog = new Dialog(profile.this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
}
