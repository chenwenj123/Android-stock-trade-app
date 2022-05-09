package com.example.stocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class portadapter extends RecyclerView.Adapter<portadapter.Viewholder>implements ItemMoveCallback.ItemTouchHelperContract{
    private Context context;
    private ArrayList<port> portArrayList;

    // Constructor
    public portadapter(Context context, ArrayList<port> portArrayList) {
        this.context = context;
        this.portArrayList = portArrayList;
    }

    @NonNull
    @Override
    public portadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull portadapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        port model = portArrayList.get(position);

        holder.ticker.setText(model.getTicker1());
        holder.share.setText(model.getShare());
        holder.market.setText(model.getMarket());
        holder.total.setText(model.getTotal1());


        holder.jiantou1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(context,stockdetail.class);
                intent2.putExtra("ticker",model.getTicker1());
                context.startActivity(intent2);
            }
        });

        if (model.getTotal1().contains("0.00")){
            holder.total.setTextColor(Color.GRAY);
        }else if (model.getTotal1().contains("-")){
            holder.total.setTextColor(Color.RED);
            holder.arrow.setImageResource(R.drawable.ic_trending_down);
            holder.arrow.setColorFilter(Color.RED);
        } else{
            holder.total.setTextColor(Color.GREEN);
            holder.arrow.setImageResource(R.drawable.ic_trending_up);
            holder.arrow.setColorFilter(Color.GREEN);
        }

    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(portArrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(portArrayList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(Viewholder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(Viewholder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return portArrayList.size();
    }

    public void removeItem(int position) {
        portArrayList.remove(position);
        notifyItemRemoved(position);

    }


    public ArrayList<port> getData() {
        return portArrayList;
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView ticker, share, market, total;
        ImageView arrow, jiantou1;
        View rowView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            ticker = itemView.findViewById(R.id.ticker2);
            share = itemView.findViewById(R.id.share);
            market=itemView.findViewById(R.id.market);
            total=itemView.findViewById(R.id.all2);
            arrow=itemView.findViewById(R.id.arrow2);
            jiantou1=itemView.findViewById(R.id.jiantou1);
        }
    }

}

