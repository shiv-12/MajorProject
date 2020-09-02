package com.example.fatchimage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SplashScreen extends AppCompatActivity {

    Animation bottomanim;
    TextView textView,texttt;
    private HashMap<String, String> mapp;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        textView = findViewById(R.id.textt);
        texttt = findViewById(R.id.texttt);
        bottomanim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        textView.setAnimation(bottomanim);
        texttt.setAnimation(bottomanim);
        databaseHandler = new DatabaseHandler(SplashScreen.this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<HashMap<String, String>> mapp = new ArrayList<>();
                mapp = databaseHandler.usersall();
                Log.d("TAG", "senddataa: " + mapp.size());
                Log.d("TAG", "senddataa: " + mapp);
                if (mapp.size() > 0) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    intent.putExtra("flag","1");
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, sign_in.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 5000);


    }
}
