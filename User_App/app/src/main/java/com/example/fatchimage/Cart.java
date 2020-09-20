package com.example.fatchimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    private ArrayList<HashMap<String, String>> maps;
    private static final String TAG = "Cart";
    private FloatingActionButton sendList;
    private RequestQueue requestQueue, requestQueue1;
    private String url = "https://shivam7898337488.000webhostapp.com/list_of_item.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        if (maps.size() > 0) {
            for (int i = 0; i < maps.size(); i++) {
                modelclass classs = new modelclass();
                classs.setId(maps.get(i).get("product_id"));
                classs.setImage(maps.get(i).get("product_image"));
                classs.setPrice(maps.get(i).get("price"));
                classs.setDesc(maps.get(i).get("description"));
                classs.setName(maps.get(i).get("product_name"));
                classs.setQty(maps.get(i).get("qty"));
                list.add(classs);

            }
            adapter = new cartAdapter(list, Cart.this);
            recyclerView.setAdapter(adapter);

            int p = 0;
            for (int i = 0; i < list.size(); i++) {
                int price = (Integer.valueOf(list.get(i).getPrice()) * Integer.valueOf(list.get(i).getQty()));
                p = price + p;
            }
            Log.d(TAG, "viewhosderr: " + p);
            setTotalamount(String.valueOf(p));
            setTotalitems(String.valueOf(list.size()));
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        new MainActivity().carttext();
//        finish();
//    }

    private void fatchhlist() {
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        String id = obj.getString("id");
                        String list = obj.getString("list");

                        Log.d(TAG, "list of data : " + id + "\n" + list);
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


    }


    private void senddata() {
        maps = databaseHandler.cartall();
        if (maps.size() > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < maps.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("product_name", maps.get(i).get("product_name"));
                    obj.put("qty", maps.get(i).get("qty"));
                    obj.put("price", maps.get(i).get("price"));
                    array.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            Log.d(TAG, "senddata: " + array.toString());
            sendordertoserver(array.toString());
        }
    }

    private void sendordertoserver(final String array) {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: " + response);
                Toast.makeText(Cart.this, "list send to the owner", Toast.LENGTH_LONG).show();

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
                return params;
            }
        };

        requestQueue.add(request);


    }


    public void setTotalamount(String totalamoun) {
        Log.d(TAG, "setTotalamount: " + totalamoun);
        totalamount.setText(totalamoun);
    }

    public void setTotalitems(String totalitem) {
        totalitems.setText(totalitem);
    }
}
