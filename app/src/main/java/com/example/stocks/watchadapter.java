package com.example.stocks;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class watchadapter extends RecyclerView.Adapter<watchadapter.Viewholder> {
    private Context context;
    private ArrayList<sharedwatchlist> watchArrayList;
    private String search;

    // Constructor
    public watchadapter(Context context, ArrayList<sharedwatchlist> watchArrayList) {
        this.context = context;
        this.watchArrayList = watchArrayList;
    }

    @NonNull
    @Override
    public watchadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watchlist, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull watchadapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        sharedwatchlist model = watchArrayList.get(position);

        holder.ticker.setText(model.getTicker());
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.total.setText(model.getTotal());



       holder.jiantou.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               Intent intent2 = new Intent(context,stockdetail.class);
               intent2.putExtra("ticker",model.getTicker());
               context.startActivity(intent2);
            }
        });

        if (model.getTotal().contains("-")){
            holder.total.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.ic_trending_down);
            holder.arrow.setColorFilter(Color.RED);
        }else if (model.getTotal().contains("0.00")){
            holder.total.setTextColor(Color.GRAY);
        } else{
            holder.total.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.ic_trending_up);
            holder.arrow.setColorFilter(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return watchArrayList.size();
    }

    public void removeItem(int position) {
        watchArrayList.remove(position);
        notifyItemRemoved(position);

    }


    public ArrayList<sharedwatchlist> getData() {
        return watchArrayList;
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ticker, name, price, total;
        ImageView arrow, jiantou;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ticker = itemView.findViewById(R.id.ticker1);
            name = itemView.findViewById(R.id.name1);
            price=itemView.findViewById(R.id.price1);
            total=itemView.findViewById(R.id.all1);
            arrow=itemView.findViewById(R.id.arrow1);
            jiantou=itemView.findViewById(R.id.jiantou);
        }
    }
}
