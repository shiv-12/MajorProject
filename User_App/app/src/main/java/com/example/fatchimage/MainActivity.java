package com.example.fatchimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<modelclass> list = new ArrayList<>();
    private recycleradapter adapter;
    private TextView cartText;
    private EditText searchedit;
    private ImageView profile;
    private RelativeLayout cartredzone, cart;
    private DatabaseHandler dbcart;
    private GoogleSignInClient mGoogleSignInClient;
    private RequestQueue requestQueue;
    private String url = "https://shivam7898337488.000webhostapp.com/getproduct.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        if (getIntent().getStringExtra("flag").equals("0")) {
            fatchuserdata();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        carttext();
        fatchdata();
        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbcart.cartall().size() > 0) {
                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    startActivity(intent);
                } else
                    Toast.makeText(MainActivity.this, "No items present in Cart", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void init() {
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        cart = findViewById(R.id.cart);
        cartText = findViewById(R.id.cartText);
        profile = findViewById(R.id.profile);
        cartredzone = findViewById(R.id.cartredzone);
        dbcart = new DatabaseHandler(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerview);
        searchedit = findViewById(R.id.searchedit);
    }

    private void fatchuserdata() {


        HashMap<String, String> params = new HashMap<>();
        params.put("userName", getIntent().getStringExtra("username"));
        params.put("userMobile", getIntent().getStringExtra("userPhoneNo"));
        dbcart.setuserdata(params);


    }


    @Override
    protected void onStart() {
        super.onStart();
        carttext();
    }

    public void carttext() {

        int size = dbcart.cartall().size();
        if (size > 0) {
            cartredzone.setVisibility(View.VISIBLE);
            cartText.setText(String.valueOf(size));
        } else
            cartredzone.setVisibility(View.GONE);


    }

    private void filter(String text) {

        ArrayList<modelclass> filteredlist = new ArrayList<>();
        for (modelclass item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);

            }
        }
        adapter.filterlist(filteredlist);

    }


    private void fatchdata() {
        final Loader loader = new Loader(MainActivity.this);
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loader.dismiss();

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        modelclass model = new modelclass();
                        JSONObject obj = array.getJSONObject(i);
                        model.setName(obj.getString("product_name"));
                        model.setDesc(obj.getString("product_description"));
                        model.setPrice(obj.getString("product_price"));
                        model.setImage(obj.getString("product_image"));
                        model.setId(obj.getString("product_id"));
                        list.add(model);

                    }

                    adapter = new recycleradapter(list, MainActivity.this);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);


    }
}
