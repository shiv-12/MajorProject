package com.example.fatchimage.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.R;
import com.example.fatchimage.Fragment.homeFragment;
import com.example.fatchimage.Fragment.searchFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView cartText, gototactt;
    private ImageView profile;
    private RelativeLayout cartredzone, cart;
    private DatabaseHandler dbcart;
    private RelativeLayout frameLayout;
    private SearchableSpinner spinner;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mref;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        carttext();
        searchcategories();


        Fragment fmm = new homeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.framelay, fmm, "homefragment").commit();

        if (getIntent().getStringExtra("flag").equals("0")) {
            fatchuserdata();
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("search", query);
                Fragment fm = new searchFragment();
                fm.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.framelay, fm, "searchfragment").commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        gototactt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbcart.cartall().size() > 0) {
                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    startActivity(intent);
                } else
                    Toast.makeText(MainActivity.this, "No items present in Cart", Toast.LENGTH_LONG).show();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.example.fatchimage.Activity.profile.class);
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

    private void searchcategories() {


        spinner.setTitle("Select Category");

        final ArrayList<String> categories = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);

        mref.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String items = snapshot.getValue(String.class);
                categories.add(items);
                Collections.sort(categories);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(categories);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Collections.sort(categories);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Collections.sort(categories);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Collections.sort(categories);
                adapter.notifyDataSetChanged();

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public void onBackPressed() {


        String tag = getSupportFragmentManager().findFragmentById(R.id.framelay).getTag();
        Log.d("sdfasdfsdg", "onBackPressed: " + tag);
        if (tag.equals("homefragment")) {
            finishAfterTransition();
        } else if (tag.equals("searchfragment")) {
            Fragment fmm = new homeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.framelay, fmm).commit();
        }


    }

    private void init() {
        cart = findViewById(R.id.cart);
        cartText = findViewById(R.id.cartText);
        profile = findViewById(R.id.profile);
        cartredzone = findViewById(R.id.cartredzone);
        dbcart = new DatabaseHandler(MainActivity.this);
        searchView = findViewById(R.id.searchbox);
        frameLayout = findViewById(R.id.framelay);
        gototactt = findViewById(R.id.gotocartt);
        spinner = findViewById(R.id.spinner);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mref = firebaseDatabase.getReference();

    }

    private void fatchuserdata() {


        HashMap<String, String> params = new HashMap<>();
        params.put("userName", getIntent().getStringExtra("username"));
        params.put("userMobile", getIntent().getStringExtra("userPhoneNo"));
        params.put("userAddress", getIntent().getStringExtra("address"));
        Log.d("TAG", "fatchuserdataaa: " + getIntent().getStringExtra("address"));
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


}
