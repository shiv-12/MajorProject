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
import com.example.fatchimage.R;
import com.example.fatchimage.JavaClass.modelclass;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.viewhosderr> {

    private List<modelclass> list;
    private Context cont;
    private DatabaseHandler dbcart;
    private static final String TAG = "cartAdapter";

    public cartAdapter(List<modelclass> list, Context cont) {
        this.list = list;
        this.cont = cont;
        dbcart = new DatabaseHandler(cont);
    }

    @NonNull
    @Override
    public cartAdapter.viewhosderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cont).inflate(R.layout.itemview, parent, false);
        viewhosderr holder = new viewhosderr(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.viewhosderr holder, int position) {

        modelclass obj = list.get(position);
        holder.name.setText(obj.getName());
        holder.desc.setText(obj.getDesc());
        holder.price.setText(obj.getPrice());
        Glide.with(cont).asBitmap().load(API.IMAGE_URL + obj.getImage()).into(holder.image);
        holder.mrp.setText(obj.getPrice());
        holder.unit.setText(obj.getUnit());
        holder.qty.setText(obj.getQty());
        int total = (Integer.valueOf(obj.getPrice()) * Integer.valueOf(obj.getQty()));
        holder.totalprice.setText("" + total);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewhosderr extends RecyclerView.ViewHolder {

        ImageView image,cancelimage;
        TextView name, desc, price, mrp, qty, totalprice,unit;
        LinearLayout addlayout, totallay, mrplay;


        public viewhosderr(@NonNull View itemView) {
            super(itemView);


            image = itemView.findViewById(R.id.iv_subcat_img);
            name = itemView.findViewById(R.id.tv_subcat_title);
            unit = itemView.findViewById(R.id.unit);
            desc = itemView.findViewById(R.id.tv_subcat_desc);
            mrp = itemView.findViewById(R.id.mrpprize);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qnty);
            cancelimage = itemView.findViewById(R.id.cancelimage);
            addlayout = itemView.findViewById(R.id.addbutton);
            totallay = itemView.findViewById(R.id.totallay);
            mrplay = itemView.findViewById(R.id.mrplayout);
            totalprice = itemView.findViewById(R.id.totalprice);

            totallay.setVisibility(View.VISIBLE);
            addlayout.setVisibility(View.GONE);
            cancelimage.setVisibility(View.VISIBLE);

            cancelimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int possition = getAdapterPosition();
                    String id = String.valueOf(list.get(possition).getId());
                    if (dbcart.removeitemfromcart(id)) {
                        list.remove(possition);
                        notifyDataSetChanged();
                        int q = 0;
                        for (int i = 0; i < list.size(); i++) {
                            int price = (Integer.valueOf(list.get(i).getPrice()) * Integer.valueOf(list.get(i).getQty()));
                            q = price + q;
                        }
                        ((Cart) cont).setTotalamount(String.valueOf(q));
                        ((Cart) cont).setTotalitems(String.valueOf(list.size()));

                    }
                }
            });


        }


    }
}
