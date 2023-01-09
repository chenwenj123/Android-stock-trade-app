package com.example.stocks;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class newsadapter extends RecyclerView.Adapter<newsadapter.Viewholder> {
    private Context context;
    private ArrayList<news> newsArrayList;
    private CardView card;

    // Constructor
    public newsadapter(Context context, ArrayList<news> newsArrayList) {
        this.context = context;
        this.newsArrayList = newsArrayList;
    }

    @NonNull
    @Override
    public newsadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newscard, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsadapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        news model = newsArrayList.get(position);
        holder.sourceName.setText(model.getSource_name());
        holder.timeAgo.setText(model.getTime_ago());
        holder.newsTitle.setText(model.getTitle_name());

        holder.NewsImage.setClipToOutline(true);
        Glide.with(context).load(model.getNewsimage()).fitCenter().centerCrop().into(holder.NewsImage);

        //gai
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.newsdialog);
                dialog.setCancelable(true);

                TextView source2 = (TextView) dialog.findViewById(R.id.source2);
                TextView retime=(TextView) dialog.findViewById(R.id.realtime);//gai
                TextView title = (TextView) dialog.findViewById(R.id.title2);
                TextView description=(TextView) dialog.findViewById(R.id.description);

                source2.setText(model.getSource_name());
                retime.setText(model.getRealtime());
                title.setText(model.getTitle_name());
                description.setText(model.getSummary());

               ImageButton chromelink=(ImageButton) dialog.findViewById(R.id.chrome);

                chromelink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getUrl()));
                        context.startActivity(browserIntent);
                    }
                });

                ImageButton facebook=(ImageButton) dialog.findViewById(R.id.facebook);
                facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="https://www.facebook.com/sharer/sharer.php?u="+model.getUrl()+"&amp;src=sdkpreparse";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });

                ImageButton twitter=(ImageButton) dialog.findViewById(R.id.twitter);
                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url="https://twitter.com/intent/tweet?text="+model.getTitle_name()+"&url="+model.getUrl();
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(browserIntent);
                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return newsArrayList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView NewsImage;
        TextView sourceName, timeAgo, newsTitle,Summary;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            NewsImage = itemView.findViewById(R.id.newsimage);
            sourceName = itemView.findViewById(R.id.source1);
            timeAgo = itemView.findViewById(R.id.timeago1);
            newsTitle = itemView.findViewById(R.id.newstitle);
            Summary=itemView.findViewById(R.id.description);
        }
    }
}
