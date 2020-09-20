package com.example.fatchimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<modelclass> list = new ArrayList<>();
    private recycleradapter adapter;
    private TextView cartText;
    private EditText searchedit;
    private RelativeLayout cartredzone, cart;
    private DatabaseHandler dbcart;
    private static final String TAG = "MainActivity";
    private RequestQueue requestQueue;
    private boolean returndata = true;
    int page = 1;
    //    GoogleSignInClient mGoogleSignInClient;
    private String url = "https://shivam7898337488.000webhostapp.com/getproduct.php?pageno=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        signininfo();

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        cart = findViewById(R.id.cart);
        cartText = findViewById(R.id.cartText);
        cartredzone = findViewById(R.id.cartredzone);
        dbcart = new DatabaseHandler(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        searchedit = findViewById(R.id.searchedit);

        carttext();
        fatchdata(page);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (utils.isLastItemDisplaying(recyclerView)) {
                    if (returndata) {
                        page++;
                        fatchdata(page);
                    }

                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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


    @Override
    protected void onStart() {
        super.onStart();
        carttext();
    }

    public void carttext() {

        int size = dbcart.cartall().size();
        if (size>0)
        {
            cartredzone.setVisibility(View.VISIBLE);
            cartText.setText(String.valueOf(size));
        }
        else
            cartredzone.setVisibility(View.GONE);



    }

    private void filter(String text) {

        Log.d(TAG, "filter: " + text);

        ArrayList<modelclass> filteredlist = new ArrayList<>();
        for (modelclass item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);

            }
        }
        adapter.filterlist(filteredlist);

    }


//    private void signininfo() {
//
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
//        if (acct != null) {
//            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//            Log.d("TAG", "signininfo: "+personName+"\n"+personEmail+"\n"+personId);
//
//            //You can also add signout button here which is bellow our oncreate method
//        }
//    }


    private void fatchdata(int page) {
        final Loader loader = new Loader(MainActivity.this);
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.GET, url+page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Data Not Found")) {
                    returndata = false;
                }

                loader.dismiss();

                Log.d("TAG", "onResponse: " + response);

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
