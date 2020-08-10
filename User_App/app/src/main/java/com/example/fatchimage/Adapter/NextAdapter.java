package com.example.fatchimage.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fatchimage.Activity.Cart;
import com.example.fatchimage.JavaClass.API;
import com.example.fatchimage.JavaClass.DatabaseHandler;
import com.example.fatchimage.JavaClass.modelclass;
import com.example.fatchimage.R;

import java.util.List;

public class NextAdapter extends RecyclerView.Adapter<NextAdapter.viewholderr> {
    private List<modelclass> list;
    private Context cont;
    private DatabaseHandler dbcart;

    public NextAdapter(List<modelclass> list, Context cont) {
        this.list = list;
        this.cont = cont;
        this.dbcart = dbcart;
    }

    @NonNull
    @Override
    public NextAdapter.viewholderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cont).inflate(R.layout.next_item_layout, parent, false);
        NextAdapter.viewholderr holder = new viewholderr(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NextAdapter.viewholderr holder, int position) {

        modelclass obj = list.get(position);
        holder.name.setText(obj.getName());

        int total = (Integer.valueOf(obj.getPrice()) * Integer.valueOf(obj.getQty()));
        holder.totalprice.setText("" + total);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholderr extends RecyclerView.ViewHolder {
        TextView name, totalprice;

        public viewholderr(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.productname);

            totalprice = itemView.findViewById(R.id.productrate);


        }
    }
}
