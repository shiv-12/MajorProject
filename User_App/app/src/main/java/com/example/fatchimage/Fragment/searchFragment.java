package com.example.fatchimage.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class searchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<modelclass> list;
    private recycleradapter adapter;
    private String search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        search = getArguments().getString("search");
        init(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        fatchdata();

        return view;
    }


    private void init(View view) {
        recyclerView = view.findViewById(R.id.searchRv);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    private void fatchdata() {
        list = new ArrayList<>();
        final Loader loader = new Loader((Activity) getContext());
        loader.startDialog();
        StringRequest request = new StringRequest(Request.Method.POST, API.SEARCH, new Response.Listener<String>() {
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
                        model.setUnit(obj.getString("unit"));
                        model.setId(obj.getString("product_id"));
                        list.add(model);

                    }

                    adapter = new recycleradapter(list, getContext());
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("search", search);
                return map;
            }
        };

        requestQueue.add(request);
    }
}