package com.example.fatchimage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class recycleradapter extends RecyclerView.Adapter<recycleradapter.viewholderr> {

    List<modelclass> list;
    Context contextl;
    DatabaseHandler dbcart;


    public recycleradapter(List<modelclass> list, Context contextl) {
        this.list = list;
        this.contextl = contextl;
        dbcart = new DatabaseHandler(contextl);
    }

    public void filterlist(List<modelclass> filterd) {
        list = filterd;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public viewholderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contextl).inflate(R.layout.itemview, parent, false);
        viewholderr holder = new viewholderr(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholderr holder, int position) {
        modelclass obj = list.get(position);
        holder.name.setText(obj.getName());
        holder.desc.setText(obj.getDesc());
        holder.price.setText(obj.getPrice());
        Glide.with(contextl).asBitmap().load("https://shivam7898337488.000webhostapp.com/productimg/" + obj.getImage()).into(holder.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholderr extends RecyclerView.ViewHolder {
        ImageView image, plus, minus;
        TextView name, desc, price, qty, update;
        LinearLayout plusminuslayout, addlayout;


        public viewholderr(@NonNull View itemView) {
            super(itemView);


            image = itemView.findViewById(R.id.iv_subcat_img);
            name = itemView.findViewById(R.id.tv_subcat_title);
            desc = itemView.findViewById(R.id.tv_subcat_desc);
            price = itemView.findViewById(R.id.mrpprize);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            qty = itemView.findViewById(R.id.qty);
            addlayout = itemView.findViewById(R.id.addbutton);
            plusminuslayout = itemView.findViewById(R.id.plusminus_layout);
            update = itemView.findViewById(R.id.update);


            addlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addlayout.setVisibility(View.GONE);
                    plusminuslayout.setVisibility(View.VISIBLE);
                    update.setVisibility(View.VISIBLE);
                    qty.setText(String.valueOf(1));
                }
            });
            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qtyy = Integer.valueOf(qty.getText().toString());
                    if (qtyy > 0) {
                        qtyy = qtyy - 1;
                        qty.setText(String.valueOf(qtyy));
                    }
                    if (qtyy == 0) {
                        plusminuslayout.setVisibility(View.GONE);
                        addlayout.setVisibility(View.VISIBLE);
                        update.setVisibility(View.GONE);
                        int possition = getAdapterPosition();
                        String id = String.valueOf(list.get(possition).getId());

                        if (dbcart.removeitemfromcart(id)) {
                            ((MainActivity) contextl).carttext();
                        }
                    }
                }
            });
            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qtyy = Integer.valueOf(qty.getText().toString());
                    qtyy = qtyy + 1;
                    if (qtyy <= 20) {
                        qty.setText(String.valueOf(qtyy));
                    } else
                        Toast.makeText(contextl, "maximum 20 products you can add", Toast.LENGTH_LONG).show();

                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.valueOf(qty.getText().toString()) > 0) {


                        int possitio = getAdapterPosition();
                        Log.d("TAGG", "onClick: " + list.get(possitio).getName() + qty.getText().toString() + "\n");
                        HashMap<String, String> params = new HashMap<>();
                        params.put("product_id", list.get(possitio).getId());
                        params.put("qty", qty.getText().toString());
                        params.put("product_name", list.get(possitio).getName());
                        params.put("description", list.get(possitio).getDesc());
                        params.put("price", list.get(possitio).getPrice());
                        params.put("product_image", list.get(possitio).getImage());

//                        if (dbcart.isInCart(list.get(possitio).getId())) {
//                            Toast.makeText(contextl, "data present already !", Toast.LENGTH_LONG).show();
//                        }
//                        else {
                        if (dbcart.setdatarow(params)) {
                            Toast.makeText(contextl, "item added to the cart", Toast.LENGTH_SHORT).show();
                            ArrayList<String> list = dbcart.readData();
                            Log.d("list", "onClick: " + list.size());
                            for (int i = 0; i < list.size(); i++) {
                                Log.d("TAGgg", "onClick: " + list.get(i));
                            }
                        }
//                        }

                    }

                    ((MainActivity) contextl).carttext();
                }
            });

        }
    }
}
