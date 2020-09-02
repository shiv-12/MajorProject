package com.example.fatchimage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatchimage.JavaClass.API;
import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.JavaClass.Loader;
import com.example.fatchimage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    private DatabaseHandler databaseHandler;
    private ArrayList<HashMap<String, String>> maps;
    private String arrayy, username, usermobile, userAddress;
    private TextView placeorder, cname, cmobile, caddress, ctotal, cdelivery, cfinal,citem;
    private Dialog dialog;
    private ImageView cancelimage;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        init();
        getdata();
        setdata();
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendordertoserver(arrayy, username, usermobile, userAddress);
            }
        });
    }

    private void setdata() {
        cname.setText(username);
        caddress.setText(userAddress);
        cmobile.setText(usermobile);
        citem.setText(getIntent().getStringExtra("itemamm"));
        ctotal.setText(getIntent().getStringExtra("totalamm"));
        cdelivery.setText(getIntent().getStringExtra("delivery"));
        cfinal.setText(getIntent().getStringExtra("finalamm"));
    }

    private void init() {
        databaseHandler = new DatabaseHandler(Checkout.this);
        maps = new ArrayList<>();
        placeorder = findViewById(R.id.placeorder);
        dialog = new Dialog(Checkout.this);
        requestQueue = Volley.newRequestQueue(Checkout.this);
        cname = findViewById(R.id.cname);
        cmobile = findViewById(R.id.cmobile);
        caddress = findViewById(R.id.caddress);
        ctotal = findViewById(R.id.ctotal);
        cdelivery = findViewById(R.id.cdelivery);
        cfinal = findViewById(R.id.cfinal);
        citem = findViewById(R.id.citem);

    }

    private void getdata() {

        HashMap<String, String> usermap;
        usermap = databaseHandler.getuser_info();
        username = usermap.get("username");
        usermobile = usermap.get("userMobile");
        userAddress = usermap.get("userAddress");

        maps = databaseHandler.cartall();
        if (maps.size() > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < maps.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("product_name", maps.get(i).get("product_name"));
                    obj.put("qty", maps.get(i).get("qty"));
                    obj.put("price", maps.get(i).get("price"));
                    obj.put("unit", maps.get(i).get("unit"));
                    array.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

                arrayy = array.toString();

            }
//            sendordertoserver(array.toString(), username.toString(), usermobile.toString(), userAddress.toString());
        }
    }

    private void sendordertoserver(final String array, final String username, final String usermobile, final String userAddress) {
        final Loader loader = new Loader(Checkout.this);
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.POST, API.LIST_OF_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.dismiss();
                databaseHandler.clearallcartdata();
                dialog.setContentView(R.layout.thankyou_dialog);
                cancelimage = dialog.findViewById(R.id.thankyoucancel);
                cancelimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(Checkout.this,MainActivity.class);
                        intent.putExtra("flag","1");
                        startActivity(intent);
                    }
                });
                dialog.getWindow().setBackgroundDrawableResource(R.color.blacktrans);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("list", array);
                params.put("username", username);
                params.put("usermobile", usermobile);
                params.put("totalprice", getIntent().getStringExtra("finalamm"));
                params.put("address", userAddress);
                return params;
            }
        };

        requestQueue.add(request);


    }
}