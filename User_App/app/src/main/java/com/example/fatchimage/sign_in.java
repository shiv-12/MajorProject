package com.example.fatchimage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sign_in extends AppCompatActivity {

    private TextView sendotp;
    private EditText username, phoneNo,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sendotp = findViewById(R.id.sendotp);
        username = findViewById(R.id.username);
        phoneNo = findViewById(R.id.phoneNo);
        address = findViewById(R.id.address);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String mobileNo = phoneNo.getText().toString();
                String addresss = address.getText().toString();
                if (name.isEmpty()) {
                    username.setError("please enter your name");
                    username.requestFocus();
                    return;
                }

                if (addresss.isEmpty())
                {
                    address.setError("please enter your address");
                    address.requestFocus();
                    return;
                }
                if (mobileNo.isEmpty() || mobileNo.length() < 10) {
                    phoneNo.setError("enter valid mobile number");
                    phoneNo.requestFocus();
                    return;
                }
                Intent intent = new Intent(sign_in.this, otpScreen.class);
                intent.putExtra("username", name);
                intent.putExtra("phoneNo", mobileNo);
                intent.putExtra("address", addresss);
                startActivity(intent);
            }
        });
    }

}
