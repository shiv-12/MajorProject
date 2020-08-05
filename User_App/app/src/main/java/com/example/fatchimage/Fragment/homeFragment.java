package com.example.fatchimage.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fatchimage.JavaClass.API;
import com.example.fatchimage.JavaClass.Loader;
import com.example.fatchimage.R;
import com.example.fatchimage.JavaClass.modelclass;
import com.example.fatchimage.Adapter.recycleradapter;
import com.example.fatchimage.JavaClass.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<modelclass> list;
    private recycleradapter adapter;
    int page = 1;
    private boolean returndata = true;
    private RequestQueue requestQueue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        fatchdata(page);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (utils.isLastItemDisplaying(recyclerView)) {
                    if (!returndata) {
                        Toast.makeText(getContext(), "No more data found !", Toast.LENGTH_LONG).show();
                    } else {
                        page++;
                        fatchdata(page);
                    }

                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    private void fatchdata(int page) {
        final Loader loader = new Loader((Activity) getContext());
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.GET, API.GET_PRODUCT + "?pageno=" + page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Data Not Found")) {
                    returndata = false;
                }

                Log.d("TAG", "onRessfsponse: " + response);

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
                        model.setUnit(obj.getString("unit"));
                        model.setId(obj.getString("product_id"));
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
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

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        list = new ArrayList<>();
        adapter = new recycleradapter(list, getContext());
        recyclerView.setAdapter(adapter);
        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }
}