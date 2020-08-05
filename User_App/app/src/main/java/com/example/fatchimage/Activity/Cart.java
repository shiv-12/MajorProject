package com.example.fatchimage.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
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
import com.example.fatchimage.Adapter.cartAdapter;
import com.example.fatchimage.JavaClass.modelclass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<modelclass> list;
    private cartAdapter adapter;
    private TextView totalamount, totalitems;
    private DatabaseHandler databaseHandler;
    public String totalprice = "1";
    private ArrayList<HashMap<String, String>> maps;
    private static final String TAG = "Cart";
    private FloatingActionButton sendList;
    private RequestQueue requestQueue, requestQueue1;
    private Dialog dialog;
    private ImageView cancelimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        if (maps.size() > 0) {
            showdata();
        }

        sendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddata();
            }
        });
        sendList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fatchhlist();
                return true;
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
            list.add(classs);

        }

        adapter = new cartAdapter(list, Cart.this);
        recyclerView.setAdapter(adapter);

        int p = 0;
        for (int i = 0; i < list.size(); i++) {
            int price = (Integer.valueOf(list.get(i).getPrice()) * Integer.valueOf(list.get(i).getQty()));
            p = price + p;
        }
        totalprice = String.valueOf(p);
        setTotalamount(String.valueOf(p));
        setTotalitems(String.valueOf(list.size()));

    }


    private void fatchhlist() {
        StringRequest request = new StringRequest(Request.Method.GET, API.LIST_OF_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String id = obj.getString("id");
                        String list = obj.getString("list");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue1.add(request);

    }

    private void init() {

        sendList = findViewById(R.id.sendList);
        recyclerView = findViewById(R.id.recyclervieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(Cart.this, RecyclerView.VERTICAL, false));
        databaseHandler = new DatabaseHandler(Cart.this);
        totalamount = findViewById(R.id.totalamount);
        totalitems = findViewById(R.id.totalitems);
        maps = new ArrayList<>();
        list = new ArrayList<>();
        maps = databaseHandler.cartall();
        requestQueue = Volley.newRequestQueue(Cart.this);
        requestQueue1 = Volley.newRequestQueue(Cart.this);
        dialog = new Dialog(Cart.this);


    }


    private void senddata() {

        HashMap<String, String> usermap;
        usermap = databaseHandler.getuser_info();
        String username = usermap.get("username");
        String usermobile = usermap.get("userMobile");
        String userAddress = usermap.get("userAddress");

        maps = databaseHandler.cartall();
        if (maps.size() > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < maps.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("product_name", maps.get(i).get("product_name"));
                    obj.put("qty", maps.get(i).get("qty"));
                    obj.put("price", maps.get(i).get("price"));
                    obj.put("unit",maps.get(i).get("unit"));
                    array.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            sendordertoserver(array.toString(), username.toString(), usermobile.toString(),userAddress.toString());
        }
    }

    private void sendordertoserver(final String array, final String username, final String usermobile,final String userAddress) {
        final Loader loader = new Loader(Cart.this);
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.POST,API.LIST_OF_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loader.dismiss();
                dialog.setContentView(R.layout.thankyou_dialog);
                cancelimage = dialog.findViewById(R.id.thankyoucancel);
                cancelimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       dialog.dismiss();
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
                params.put("totalprice", totalprice);
                params.put("address", userAddress);
                return params;
            }
        };

        requestQueue.add(request);


    }

    public void setTotalamount(String totalamoun) {
        totalprice = totalamoun;
        totalamount.setText(totalamoun);
    }

    public void setTotalitems(String totalitem) {
        totalitems.setText(totalitem);
    }
}
