package com.example.stocks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.core.HIChartView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.highsoft.highcharts.core.*;
import com.highsoft.highcharts.core.*;


public class stockdetail extends AppCompatActivity {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    TextView mainticker;
    TextView maincompany;
    TextView mainprice;
    TextView maintotal;
    ImageView arrow;
    TextView mainopen;
    TextView mainclose;
    TextView mainhigh;
    TextView mainlow;
    TextView mainipo;
    TextView mainindustry;
    TextView mainpeers;
    TextView mainweb;

    static String stock;

    private RecyclerView newsRV;
    private ArrayList<news> newsArrayList;
    public ArrayList<Integer> newlist1;
    public ArrayList portfolio;
    static Integer number=1;

    RecyclerView recyclerView1; //company peer
    LinearLayoutManager HorizontalLayout;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    int RecyclerViewItemPosition;

    public static ArrayList<String> watch;
    public static String total;
    public static String price;
    public static String name;
    public static double dp;
    public static double c;
    public String shares;
    public Integer intshares;
    public double totalamount;

    public Integer s1;
    public double share2;

    final Context context = this;
    private Button button;
    private ProgressBar spinner;
    public LinearLayout linlaHeaderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);

        setTheme(R.style.stockdetails);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockdetail);
        Toolbar myToolbar = findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        linlaHeaderProgress.bringToFront();

        //get search ticker
        Intent myIntent = getIntent();
        String symbol = myIntent.getStringExtra("ticker");
        stock=symbol;

        getSupportActionBar().setTitle(symbol);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        makeApiCall(symbol);
        makepriceApiCall(symbol);
        makesocialApiCall(symbol);
        makenewsApiCall(symbol);
        makepeerApiCall(stock);


        //< get elements >
        TabLayout tabLayout=findViewById(R.id.tabs);
        ViewPager2 viewPager2=findViewById(R.id.view_pager);

        AdapterDemo adapter = new AdapterDemo(this);
        viewPager2.setAdapter(adapter);

        //Tab layout
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    final int[] tabIcons = {R.drawable.ic_chart_line, R.drawable.ic_clock_time_three};
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setIcon(tabIcons[position]);

                    }
                }).attach();

        WebView recom = (WebView) findViewById(R.id.recommend);
        recom.loadUrl("file:///android_asset/recommend.html");
        recom.getSettings().setAllowFileAccess(true);
        recom.getSettings().setJavaScriptEnabled (true);
        recom.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                recom.loadUrl("javascript:getrecomchartapi('" + symbol + "')");
                //if passing in an object. Mapping may need to take place
            }
        });

        SharedPreferences port = getSharedPreferences("Portfolio", MODE_PRIVATE);
        SharedPreferences.Editor editor8 = port.edit();

        SharedPreferences money1 = getSharedPreferences("money", MODE_PRIVATE);
        SharedPreferences.Editor editor5 = money1.edit();

        //port.edit().remove("TSLA").commit();
       // money1.edit().remove("initial").commit();
        if(money1.contains("initial")){
            money1.getString("initial","");
            Log.e("pp",money1.getString("initial",""));
        }else{
            editor5.putString("initial","25000.00");
            editor5.apply();
            editor5.commit();
        }

        String value=port.getString(stock,"");
        if(value.contains(stock)){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> list = gson.fromJson(port.getString(stock, ""), type); //change string to arraylist
            TextView shareOwn= (TextView) findViewById(R.id.shareown);
            shareOwn.setText(list.get(1));
            TextView avgcost= (TextView) findViewById(R.id.avgcost);
            avgcost.setText("$"+list.get(2));
            TextView totalcost= (TextView) findViewById(R.id.totalcost);
            totalcost.setText("$"+list.get(3));
            TextView change= (TextView) findViewById(R.id.change);
            change.setText("$"+list.get(4));
            TextView market= (TextView) findViewById(R.id.marketvalue);
            market.setText("$"+list.get(5));

            Double change1=Double.parseDouble(list.get(4));
            if(change1>0){
                change.setTextColor(Color.GREEN);
                market.setTextColor(Color.GREEN);
            }else if(change1==0){
                change.setTextColor(Color.GRAY);
                market.setTextColor(Color.GRAY);
            } else{
                change.setTextColor(Color.RED);
                market.setTextColor(Color.RED);
            }


        }else{
            TextView shareOwn= (TextView) findViewById(R.id.shareown);
            shareOwn.setText("0");
            TextView avgcost= (TextView) findViewById(R.id.avgcost);
            avgcost.setText("$0.00");
            TextView totalcost= (TextView) findViewById(R.id.totalcost);
            totalcost.setText("$0.00");
            TextView change= (TextView) findViewById(R.id.change);
            change.setText("$0.00");
            TextView market= (TextView) findViewById(R.id.marketvalue);
            market.setText("$0.00");
        }


        //trade button dialog
        button = (Button) findViewById(R.id.trade);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.trade_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.detail_trade_dialog_title);
                text.setText("Trade "+name+" shares");

                TextView initial = (TextView) dialog.findViewById(R.id.detail_trade_calculate);
                String calculate1="0.0"+"*"+"$"+String.format("%.2f", c)+"/share = "+String.format("%.2f", c);
                initial.setText(calculate1);

                TextView balance = (TextView) dialog.findViewById(R.id.detail_trade_balance);
                String wallet=money1.getString("initial","");
                balance.setText("$"+wallet+" to buy "+stock);

                EditText editText=(EditText) dialog.findViewById(R.id.detail_trade_Et);
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        TextView text1 = (TextView) dialog.findViewById(R.id.detail_trade_calculate);
                        //s1 integer shares string
                        s1 = Integer.parseInt(s.toString());
                        DecimalFormat f = new DecimalFormat("##0.0");
                        shares = f.format(s1); //change s1 to string with format

                       //price
                        DecimalFormat df1=new DecimalFormat("#.00");
                        String price1=df1.format(c);

                        totalamount=s1*c;
                        String totalamount1=df.format(totalamount);

                        String calculate=shares+"*"+"$"+price1+"/share = "+totalamount1;
                        text1.setText(calculate);
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


                Button buyButton = (Button) dialog.findViewById(R.id.detail_trade_buy_btn);
                // if button is clicked, close the custom dialog
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //list{stock,shares,avgcost,totalcost,change,market}
                        String stockvalue=port.getString(stock,"");
                        String money=money1.getString("initial","");
                        Double money2=Double.parseDouble(money);
                         if(s1==null){
                             Toast.makeText(stockdetail.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();

                         }else if(s1*c>money2){
                             Toast.makeText(stockdetail.this, "Not enough money to buy", Toast.LENGTH_SHORT).show();

                         } else if(stockvalue.contains(stock)){
                             Gson gson = new Gson();
                             Type type = new TypeToken<ArrayList<String>>() {
                             }.getType();
                             //change string to string arraylist
                             ArrayList<String> newlist = gson.fromJson(port.getString(stock, ""), type);
                             double share1=Double.parseDouble(newlist.get(1));
                             double total1=Double.parseDouble(newlist.get(3));

                             double newshare=share1+s1;
                             double newavg=(total1+s1*c)/(newshare);
                             double newtotalcost=total1+s1*c;
                             double newchange=c-newavg;
                             double newmarket=c*newshare;

                             String newshare1=String.format("%.2f",newshare);
                             String newavg1= String.format("%.2f",newavg);
                             String newtotalcost1= String.format("%.2f",newtotalcost);
                             String newchange1= String.format("%.2f",newchange);
                             String newmarket1= String.format("%.2f",newmarket);

                             //保留两位小数?

                             ArrayList newnew= new ArrayList();
                             newnew.add(stock);
                             newnew.add(newshare1);
                             newnew.add(newavg1);
                             newnew.add(newtotalcost1);
                             newnew.add(newchange1);
                             newnew.add(newmarket1);
                             newnew.add(c);

                             Gson gson1 = new Gson();
                             String json1 = gson1.toJson(newnew);
                             editor8.putString(stock, json1);
                             editor8.apply();
                             editor8.commit();

                             //wallet
                             String wallet2=money1.getString("initial","");
                             Double wallet3=Double.parseDouble(wallet2);
                             Double newwallet1=wallet3-s1*c;
                             editor5.putString("initial", String.format("%.2f", newwallet1));
                             editor5.apply();
                             editor5.commit();

                             TextView shareOwn= (TextView) findViewById(R.id.shareown);
                             shareOwn.setText(newshare1);
                             TextView avgcost= (TextView) findViewById(R.id.avgcost);
                             avgcost.setText("$"+newavg1);
                             TextView totalcost= (TextView) findViewById(R.id.totalcost);
                             totalcost.setText("$"+newtotalcost1);
                             TextView change= (TextView) findViewById(R.id.change);
                             change.setText("$"+newchange1);
                             TextView market= (TextView) findViewById(R.id.marketvalue);
                             market.setText("$"+newmarket1);

                             if(newchange==0.00){
                                 change.setTextColor(Color.GRAY);
                                 market.setTextColor(Color.GRAY);
                             }else if(newchange>0){
                                 change.setTextColor(Color.GREEN);
                                 market.setTextColor(Color.GREEN);
                             }else {
                                 change.setTextColor(Color.RED);
                                 market.setTextColor(Color.RED);
                             }

                             //dialog
                             final Dialog dialog1 = new Dialog(context);
                             dialog1.setContentView(R.layout.trade_success);
                             dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                             TextView text = (TextView) dialog1.findViewById(R.id.show_message);
                             text.setText("You have successfully bought "+s1+" shares of "+stock);
                             Button doneButton = (Button) dialog1.findViewById(R.id.done);
                             doneButton.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     dialog1.dismiss();
                                     dialog.dismiss();
                                 }
                             });
                             dialog1.show();

                         }else{
                             Double currentbuy=s1*c;
                             Double avgcost=currentbuy/s1;
                             Double change=c-avgcost;

                             String avgcost8=String.format("%.2f",avgcost);
                             String change8=String.format("%.2f",change);
                             String currentbuy8=String.format("%.2f",currentbuy);

                             portfolio=new ArrayList();
                             portfolio.add(stock);
                             portfolio.add(shares);
                             portfolio.add(avgcost); //avgcost
                             portfolio.add(currentbuy);  //total cost
                             portfolio.add(change);  //change avgcost/share-current price
                             portfolio.add(currentbuy); //market
                             portfolio.add(c);

                             String key=stock;
                             Gson gson = new Gson();
                             String json = gson.toJson(portfolio);
                             editor8.putString(key, json);
                             editor8.apply();
                             editor8.commit();

                             //25000
                             String wallet=money1.getString("initial","");
                             Double wallet1=Double.parseDouble(wallet);
                             Double newwallet=wallet1-currentbuy;
                             editor5.putString("initial", String.format("%.2f", newwallet));
                             editor5.apply();
                             editor5.commit();

                             TextView shareOwn= (TextView) findViewById(R.id.shareown);
                             shareOwn.setText(shares);
                             TextView avgcost1= (TextView) findViewById(R.id.avgcost);
                             avgcost1.setText("$"+avgcost8);
                             TextView totalcost1= (TextView) findViewById(R.id.totalcost);
                             totalcost1.setText("$"+currentbuy8);
                             TextView change1= (TextView) findViewById(R.id.change);
                             change1.setText("$"+change8);
                             TextView market= (TextView) findViewById(R.id.marketvalue);
                             market.setText("$"+currentbuy8);

                             if(change==0.00){
                                 change1.setTextColor(Color.GRAY);
                                 market.setTextColor(Color.GRAY);
                             }else if(change>0){
                                 change1.setTextColor(Color.GREEN);
                                 market.setTextColor(Color.GREEN);
                             }else {
                                 change1.setTextColor(Color.RED);
                                 market.setTextColor(Color.RED);
                             }

                             //dialog
                             final Dialog dialog1 = new Dialog(context);
                             dialog1.setContentView(R.layout.trade_success);
                             dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                             TextView text = (TextView) dialog1.findViewById(R.id.show_message);
                             text.setText("You have successfully bought "+s1+" shares of "+stock);
                             Button doneButton = (Button) dialog1.findViewById(R.id.done);
                             doneButton.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     dialog1.dismiss();
                                     dialog.dismiss();
                                 }
                             });
                             dialog1.show();

                         }

                    }
                });

                Button sellButton = (Button) dialog.findViewById(R.id.detail_trade_sell_btn);
                // if button is clicked, close the custom dialog
                sellButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //list{stock,shares,avgcost,totalcost,change,market,price}
                        String stockvalue=port.getString(stock,"");
                        Gson gson2 = new Gson();
                        Type type1 = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        ArrayList<String> list1 = gson2.fromJson(port.getString(stock, ""), type1);
                        if(port.contains(stock)){
                            share2=Double.parseDouble(list1.get(1));
                        }else{
                            share2=0;
                        }

                        if(share2==0){
                            Toast.makeText(stockdetail.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                        }
                        else if(s1==null){
                            Toast.makeText(stockdetail.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                        }else if(s1==share2){
                            port.edit().remove(stock).commit();
                            //wallet
                            String wallet2=money1.getString("initial","");
                            Double wallet3=Double.parseDouble(wallet2);
                            Double newwallet1=wallet3+s1*c;
                            editor5.putString("initial", String.format("%.2f", newwallet1));
                            editor5.apply();
                            editor5.commit();

                            TextView shareOwn= (TextView) findViewById(R.id.shareown);
                            shareOwn.setText("0");
                            TextView avgcost= (TextView) findViewById(R.id.avgcost);
                            avgcost.setText("$0.00");
                            TextView totalcost= (TextView) findViewById(R.id.totalcost);
                            totalcost.setText("$0.00");
                            TextView change= (TextView) findViewById(R.id.change);
                            change.setText("$0.00");
                            TextView market= (TextView) findViewById(R.id.marketvalue);
                            market.setText("$0.00");

                            //dialog
                            final Dialog dialog1 = new Dialog(context);
                            dialog1.setContentView(R.layout.trade_success);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView text = (TextView) dialog1.findViewById(R.id.show_message);
                            text.setText("You have successfully sold "+s1+" shares of "+stock);
                            Button doneButton = (Button) dialog1.findViewById(R.id.done);
                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.dismiss();
                                    dialog.dismiss();
                                }
                            });
                            dialog1.show();


                        }else if(s1>share2){
                            Toast.makeText(stockdetail.this, "Not enough shares to sale", Toast.LENGTH_SHORT).show();
                        } else{
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            //change string to string arraylist
                            ArrayList<String> newlist = gson.fromJson(port.getString(stock, ""), type);
                            double share1=Double.parseDouble(newlist.get(1));
                            double total1=Double.parseDouble(newlist.get(3));

                            double newshare=share1-s1;
                            double newavg=(total1-s1*c)/(newshare);
                            double newtotalcost=total1-s1*c;
                            double newchange=c-newavg;
                            double newmarket=c*newshare;

                            String newshare1=String.format("%.2f",newshare);
                            String newavg1= String.format("%.2f",newavg);
                            String newtotalcost1= String.format("%.2f",newtotalcost);
                            String newchange1= String.format("%.2f",newchange);
                            String newmarket1= String.format("%.2f",newmarket);

                            //保留两位小数?

                            ArrayList newnew1= new ArrayList();
                            newnew1.add(stock);
                            newnew1.add(newshare1);
                            newnew1.add(newavg1);
                            newnew1.add(newtotalcost1);
                            newnew1.add(newchange1);
                            newnew1.add(newmarket1);
                            newnew1.add(c);

                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(newnew1);
                            editor8.putString(stock, json1);
                            editor8.apply();
                            editor8.commit();

                            //wallet
                            String wallet2=money1.getString("initial","");
                            Double wallet3=Double.parseDouble(wallet2);
                            Double newwallet1=wallet3+s1*c;
                            editor5.putString("initial", String.format("%.2f", newwallet1));
                            editor5.apply();
                            editor5.commit();

                            TextView shareOwn= (TextView) findViewById(R.id.shareown);
                            shareOwn.setText(newshare1);
                            TextView avgcost= (TextView) findViewById(R.id.avgcost);
                            avgcost.setText("$"+newavg1);
                            TextView totalcost= (TextView) findViewById(R.id.totalcost);
                            totalcost.setText("$"+newtotalcost1);
                            TextView change= (TextView) findViewById(R.id.change);
                            change.setText("$"+newchange1);
                            TextView market= (TextView) findViewById(R.id.marketvalue);
                            market.setText("$"+newmarket1);

                            if(newchange==0.00){
                                change.setTextColor(Color.GRAY);
                                market.setTextColor(Color.GRAY);

                            }else if(newchange>0){
                                change.setTextColor(Color.GREEN);
                                market.setTextColor(Color.GREEN);
                            }else {
                                change.setTextColor(Color.RED);
                                market.setTextColor(Color.RED);
                            }

                            //dialog
                            final Dialog dialog1 = new Dialog(context);
                            dialog1.setContentView(R.layout.trade_success);
                            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView text = (TextView) dialog1.findViewById(R.id.show_message);
                            text.setText("You have successfully sold "+s1+" shares of "+stock);
                            Button doneButton = (Button) dialog1.findViewById(R.id.done);
                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    dialog1.dismiss();
                                }
                            });
                            dialog1.show();
                        }

                    }
                });
                dialog.show();
            }
        });


        WebView earning = (WebView) findViewById(R.id.earning);
        earning.loadUrl("file:///android_asset/earning.html");
        earning.getSettings().setAllowFileAccess(true);
        earning.getSettings().setJavaScriptEnabled (true);
        earning.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                earning.loadUrl("javascript:getearningchartapi('" + symbol + "')"); //if passing in an object. Mapping may need to take place

                linlaHeaderProgress.setVisibility(View.GONE);
                setProgressBarIndeterminateVisibility(false);
            }
        });

    }


    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if(number==1){
                    startActivity(new Intent(this,MainActivity.class));
                }else {
                  number=number-1;
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //favorite button
    ArrayList<String> watchlist = new ArrayList<String>();
    ArrayList<String> keyvalue= new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.button, menu);
        MenuItem menuItem = menu.findItem(R.id.action_favorite);

        SharedPreferences sharedPref = getSharedPreferences("Local", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String value=sharedPref.getString(stock,"");
        if(value.contains(stock)){
            menuItem.setIcon(R.drawable.ic_star);
            Drawable drawable = menuItem.getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);

        }else{
            menuItem.setIcon(R.drawable.ic_star_outline);
            Drawable drawable = menuItem.getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
        }


        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //initialize localstorage
                SharedPreferences sharedPref = getSharedPreferences("Local", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String value=sharedPref.getString(stock,"");

                if(value.contains(stock)){
                    editor.remove(stock);
                    editor.commit();
                    menuItem.setIcon(R.drawable.ic_star_outline);
                    Drawable drawable = menuItem.getIcon();
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(stockdetail.this, stock+" is removed from favorites", Toast.LENGTH_SHORT).show();


                }else{
                    ArrayList favorite=new ArrayList();
                    favorite.add(stock);
                    favorite.add(name);
                    favorite.add(price);
                    favorite.add(total);

                    String key=stock;
                    Gson gson = new Gson();
                    String json = gson.toJson(favorite);
                    editor.putString(key, json);
                    editor.apply();
                    editor.commit();

                    menuItem.setIcon(R.drawable.ic_star);
                    Drawable drawable = menuItem.getIcon();
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(android.R.color.holo_red_dark), PorterDuff.Mode.SRC_ATOP);
                    Toast.makeText(stockdetail.this,stock+" is added to favorites", Toast.LENGTH_LONG).show();

                }

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }


    // api call fetch stockdetail
    public static class ApiCall {
        private static stockdetail.ApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public ApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized stockdetail.ApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new stockdetail.ApiCall(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }
        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public static void make(Context ctx, String symbol,Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url = "https://mynewhw8.wn.r.appspot.com/stockdetail/"+symbol;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            stockdetail.ApiCall.getInstance(ctx).addToRequestQueue(stringRequest);

        }
    }

    //select data
    private void makeApiCall(String text) {
        stockdetail.ApiCall.make(this, text, new Response.Listener<String>() {
            String dateTime;
            SimpleDateFormat simpleDateFormat;
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String ticker = jsonObject.getString("ticker");
                    name = jsonObject.getString("name");
                    String logo = jsonObject.getString("logo");
                    String ipo= jsonObject.getString("ipo");
                    String industry= jsonObject.getString("finnhubIndustry");
                    String web= jsonObject.getString("weburl");

                    Log.e("name", jsonObject.getString("name") );

                    //transfer time
                    String year=ipo.substring(0,4);
                    Log.e("year", year );
                    String month=ipo.substring(5,7);
                    String day=ipo.substring(8,10);
                   String newipo= day+"-"+month+"-"+year;
                    Log.e("date", newipo );

                    mainticker = (TextView) findViewById(R.id.ticker);
                    mainticker.setText(ticker);
                    maincompany = (TextView) findViewById(R.id.company);
                    maincompany.setText(name);
                    ImageView logoimage = (ImageView) findViewById(R.id.logo);
                    Picasso.get().load(logo).into(logoimage);


                    mainipo=(TextView) findViewById(R.id.ipodate);
                    mainipo.setText(newipo);
                    mainindustry=(TextView) findViewById(R.id.industry);
                    mainindustry.setText(industry);
                    mainweb=(TextView) findViewById(R.id.web);
                    mainweb.setText(web);
                    TextView company=(TextView) findViewById(R.id.companyname);
                    company.setText(name);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }


    // fetch latestprice
    public static class priceApiCall {
        private static stockdetail.priceApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public priceApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized stockdetail.priceApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new stockdetail.priceApiCall(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }
        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public static void make(Context ctx, String symbol,Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url = "https://mynewhw8.wn.r.appspot.com/latestprice/"+symbol;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            stockdetail.priceApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void makepriceApiCall(String text) {
        stockdetail.priceApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    c= jsonObject.getDouble("c");
                    double d= jsonObject.getDouble("d");
                    dp= jsonObject.getDouble("dp");
                    double h= jsonObject.getDouble("h");
                    double l= jsonObject.getDouble("l");
                    double o= jsonObject.getDouble("o");
                    double pc= jsonObject.getDouble("pc");

                    price= String.format("%.2f", c);
                    total= "$"+df.format(d)+" "+"("+df.format(dp)+"%"+")";
                    String openprice="$"+df.format(o);
                    String highprice="$"+df.format(h);
                    String lowprice="$"+df.format(l);
                    String prevprice="$"+df.format(pc);

                    Log.e("c", jsonObject.getString("c") );
                    Log.e("d", price );


                    mainprice = (TextView) findViewById(R.id.price);
                    mainprice.setText(price);
                    maintotal = (TextView) findViewById(R.id.total);
                    maintotal.setText(String.valueOf(total));
                    arrow=(ImageView) findViewById(R.id.arrow);
                    mainhigh=(TextView) findViewById(R.id.highprice);
                    mainhigh.setText(highprice);
                    mainlow=(TextView) findViewById(R.id.lowprice);
                    mainlow.setText(lowprice);
                    mainopen=(TextView) findViewById(R.id.openprice);
                    mainopen.setText(openprice);
                    mainclose=(TextView) findViewById(R.id.prevclose);
                    mainclose.setText(prevprice);

                    if(dp>0){
                        maintotal.setTextColor(Color.GREEN);
                        arrow.setImageResource(R.drawable.ic_trending_up);
                        arrow.setColorFilter(Color.GREEN);

                    }else{
                        maintotal.setTextColor(Color.RED);
                        arrow.setImageResource(R.drawable.ic_trending_down);
                        arrow.setColorFilter(Color.RED);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    // fetch companypeer
    public static class peerApiCall {
        private static stockdetail.peerApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public peerApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized stockdetail.peerApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new stockdetail.peerApiCall(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }
        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public static void make(Context ctx, String symbol,Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url = "https://mynewhw8.wn.r.appspot.com/companypeers/"+symbol;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            stockdetail.peerApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void makepeers(String response){
        try {
            JSONArray peerarray = new JSONArray(response);

            int haru=peerarray.length();
            Log.e("haa", String.valueOf(haru));
            Log.e("res", String.valueOf(peerarray));

            ArrayList<String> peerlist = new ArrayList<>();
            for (int i = 0; i <peerarray.length(); i++){
                String mypeer= peerarray.getString(i);
                Log.e("sjsj",mypeer);
                peerlist.add(mypeer);
            }
            RecyclerView recyclerView = findViewById(R.id.peerview1);
            LinearLayoutManager mRecyclerViewLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(mRecyclerViewLayoutManager);
            mRecyclerViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, peerlist);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void makepeerApiCall(String text) {
        stockdetail.peerApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                makepeers(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }





    // fetch social
    public static class socialApiCall {
        private static stockdetail.socialApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public socialApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized stockdetail.socialApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new stockdetail.socialApiCall(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }
        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public static void make(Context ctx, String symbol,Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url = "https://mynewhw8.wn.r.appspot.com/socialsentimentdata/"+symbol;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            stockdetail.socialApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void makesocialApiCall(String text) {
        stockdetail.socialApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray reddit= jsonObject.getJSONArray("reddit");
                    JSONArray twitter=jsonObject.getJSONArray("twitter");
                    int tred=0;
                    int ttwit=0;
                    int pred=0;
                    int ptwit=0;
                    int nred=0;
                    int ntwit=0;

                    for(int i=0; i<reddit.length();i++){
                        tred= tred+reddit.getJSONObject(i).getInt("mention");
                       ttwit=ttwit+twitter.getJSONObject(i).getInt("mention");
                       pred=pred+reddit.getJSONObject(i).getInt("positiveMention");
                       ptwit=ptwit+twitter.getJSONObject(i).getInt("positiveMention");
                       nred=nred+reddit.getJSONObject(i).getInt("negativeMention");
                       ntwit=ntwit+twitter.getJSONObject(i).getInt("negativeMention");
                    }

                    String totalred=Integer.toString(tred);
                    String totaltwit=Integer.toString(ttwit);
                    String posred=Integer.toString(pred);
                    String postwit=Integer.toString(ptwit);
                    String negred=Integer.toString(nred);
                    String negtwit=Integer.toString(ntwit);

                    TextView redtotal = (TextView) findViewById(R.id.totalred);
                    redtotal.setText(totalred);
                    TextView twittotal=(TextView) findViewById(R.id.totaltwit);
                    twittotal.setText(totaltwit);
                    TextView positivered=(TextView) findViewById(R.id.posred);
                    positivered.setText(posred);
                    TextView positivetwit=(TextView) findViewById(R.id.postwit);
                    positivetwit.setText(postwit);
                    TextView negared=(TextView) findViewById(R.id.negred);
                    negared.setText(negred);
                    TextView negatwit=(TextView) findViewById(R.id.negtwit);
                    negatwit.setText(negtwit);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

//fetch news
    public static class newsApiCall {
        private static stockdetail.newsApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public newsApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized stockdetail.newsApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new stockdetail.newsApiCall(context);
            }
            return mInstance;
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            }
            return mRequestQueue;
        }
        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }

        public static void make(Context ctx, String symbol,Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url = "https://mynewhw8.wn.r.appspot.com/news/"+symbol;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    listener, errorListener);
            stockdetail.newsApiCall.getInstance(ctx).addToRequestQueue(stringRequest);
        }
    }

    private void makeresponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object1 = jsonArray.getJSONObject(0);
                    String category1 = object1.getString("category");
                    Integer datetime1 = object1.getInt("datetime");
                    String headline1 = object1.getString("headline");
                    Integer id1 = object1.getInt("id");
                    String image1 = object1.getString("image");
                    String related1 = object1.getString("related");
                    String source1 = object1.getString("source");
                    String summary1 = object1.getString("summary");
                    String url1 = object1.getString("url");

                    ImageView newslogo1 = (ImageView) findViewById(R.id.newsimage1);
                    newslogo1.setClipToOutline(true);
                    Picasso.get().load(image1).into(newslogo1);

                    TextView newssource = (TextView) findViewById(R.id.source);
                    newssource.setText(source1);

                    //calculate time
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long now = timestamp.getTime() / 1000;
                    Long diff = (now - datetime1) / 3600;
                    String timenow = diff.toString() + " hours ago";
                    TextView newstime = (TextView) findViewById(R.id.timeago);
                    newstime.setText(timenow);

                    TextView newstitle = (TextView) findViewById(R.id.headline);
                    newstitle.setText(headline1);

                    // here we have created new array list and added data to it.
                    newsArrayList = new ArrayList<>();
                    for (int i = 1; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String category = object.getString("category");
                        Integer datetime = object.getInt("datetime");
                        String headline = object.getString("headline");
                        Integer id = object.getInt("id");
                        String image = object.getString("image");
                        String related = object.getString("related");
                        String source = object.getString("source");
                        String summary = object.getString("summary");
                        String url = object.getString("url");

                        Calendar mydate = Calendar.getInstance();
                        String realtime = mydate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) +" "+mydate.get(Calendar.DAY_OF_MONTH)+", "+mydate.get(Calendar.YEAR);

                        Long diff1 = (now - datetime) / 3600;
                        String timenow1 = diff1.toString() + " hours ago";
                        newsArrayList.add(new news(source,timenow1,headline,image,summary,url,realtime));

                       // Log.e("ahhhh", headline);
                    }

                    //news adapter
                    newsRV = findViewById(R.id.idRVNews);
                    //newsArrayList = new ArrayList<>();
                    // we are initializing our adapter class and passing our arraylist to it.
                    newsadapter Newsadapter = new newsadapter(this, newsArrayList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    newsRV.setLayoutManager(new LinearLayoutManager(this));
                    newsRV.setAdapter(Newsadapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            private void makenewsApiCall(String text) {
            stockdetail.newsApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                makeresponse(response);
                }

        }, new Response.ErrorListener()

    {
        @Override
        public void onErrorResponse (VolleyError error){
    }
        });
    }


}

