package com.example.fatchimage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fatchimage.Adapter.NextAdapter;
import com.example.fatchimage.Adapter.cartAdapter;
import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.JavaClass.modelclass;
import com.example.fatchimage.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Next extends AppCompatActivity {

    private List<modelclass> listt;
    private RecyclerView recyclerView;
    private NextAdapter adapter;
    private String totalamm,finalamm,delivery,itemamm;
    private ArrayList<HashMap<String, String>> maps;
    private static final String TAG = "Next";
    private DatabaseHandler databaseHandler;
    private TextView nexttotalamount,nextcontinueshopping,more,des,rs,nextdelivery,nextfinal,nextcheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        init();

        if (maps.size() > 0) {
            showdata();
        }

        nextcontinueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Next.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        nextcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Next.this, Checkout.class);
                intent.putExtra("totalamm",totalamm);
                intent.putExtra("delivery",delivery);
                intent.putExtra("finalamm",finalamm);
                intent.putExtra("itemamm",itemamm);
                startActivity(intent);
            }
        });

    }

    private void showdata() {
        for (int i = 0; i < maps.size(); i++) {
            modelclass classs = new modelclass();
            classs.setId(maps.get(i).get("product_id"));
            classs.setImage(maps.get(i).get("product_image"));
            classs.setPrice(maps.get(i).get("price"));
            classs.setDesc(maps.get(i).get("description"));
            classs.setName(maps.get(i).get("product_name"));
            classs.setQty(maps.get(i).get("qty"));
            classs.setUnit(maps.get(i).get("unit"));
            listt.add(classs);

        }

        adapter = new NextAdapter(listt, Next.this);
        recyclerView.setAdapter(adapter);
        int p = 0;
        for (int i = 0; i < listt.size(); i++) {
            int price = (Integer.valueOf(listt.get(i).getPrice()) * Integer.valueOf(listt.get(i).getQty()));
            p = price + p;
        }
        nexttotalamount.setText(String.valueOf(p));
        totalamm = String.valueOf(p);
        itemamm = String.valueOf(listt.size());
        int moree = 200-p;
        if (moree<=0)
        {
            des.setVisibility(View.GONE);
            rs.setVisibility(View.GONE);
            more.setText("Hurry!  You Got Free delivery");
            nextdelivery.setText("0");
            nextfinal.setText(String.valueOf(p));
            finalamm = String.valueOf(p);
            delivery = String.valueOf(0);

        }
        else {
            more.setText(String.valueOf(moree));
            nextdelivery.setText("20");
            nextfinal.setText(String.valueOf(p+20));
            finalamm = String.valueOf(p+20);
            delivery = String.valueOf(20);
        }

    }

    private void init() {
        listt = new ArrayList<>();
        databaseHandler = new DatabaseHandler(Next.this);
        recyclerView = findViewById(R.id.nextRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(Next.this, RecyclerView.VERTICAL, false));
        maps = new ArrayList<>();
        maps = databaseHandler.cartall();
        nexttotalamount = findViewById(R.id.nexttotalamount);
        nextcontinueshopping = findViewById(R.id.nextcontinueshopping);
        more = findViewById(R.id.more);
        des = findViewById(R.id.des);
        rs = findViewById(R.id.rs);
        nextdelivery = findViewById(R.id.nextdelivery);
        nextfinal = findViewById(R.id.nextfinal);
        nextcheckout = findViewById(R.id.nextcheckout);

    }
}