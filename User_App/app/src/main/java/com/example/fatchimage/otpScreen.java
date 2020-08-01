package com.example.fatchimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.security.AuthProvider;
import java.util.concurrent.TimeUnit;

public class otpScreen extends AppCompatActivity {

    private EditText otp;
    private TextView verify;
    private String systemgeneratedcode;
    private ProgressBar progressBar;
    private String mobileNo, username,useraddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_screen);
        otp = findViewById(R.id.phoneNum);
        verify = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);
        mobileNo = getIntent().getStringExtra("phoneNo");
        username = getIntent().getStringExtra("username");
        useraddress = getIntent().getStringExtra("address");
        Log.d("TAG", "onCreeeeate: "+useraddress);

        sendverificationcode(mobileNo);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpp = otp.getText().toString();
                if (otpp == null || otpp.length() < 6) {
                    Toast.makeText(otpScreen.this, "Enter valid otp", Toast.LENGTH_LONG).show();
                } else
                    verifycode(otpp);

            }
        });

    }


    private void sendverificationcode(String mobileNo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks


    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new
            PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    systemgeneratedcode = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        verifycode(code);
                    } else
                        Toast.makeText(otpScreen.this, "Enter your Code", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(otpScreen.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            };

    private void verifycode(String codebyUser) {

        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemgeneratedcode, codebyUser);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(otpScreen.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(otpScreen.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("username", username);
                            intent.putExtra("userPhoneNo", mobileNo);
                            intent.putExtra("address", useraddress);
                            intent.putExtra("flag","0");
                            startActivity(intent);
                        } else
                            Toast.makeText(otpScreen.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

    }
}
