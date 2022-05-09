package com.example.stocks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.SearchAutoComplete;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//reference:https://www.truiton.com/2018/06/android-autocompletetextview-suggestions-from-webservice-call/
public class MainActivity extends AppCompatActivity {
    private RecyclerView watchRV;
    private RecyclerView portRV;
    private ArrayList<sharedwatchlist> watchArrayList;
    watchadapter Watchadapter;
    portadapter Portadapter;
    public ArrayList<String> keylist;
    public ArrayList<String> portlist;
    public static double c;
    private ProgressBar spinner;
    public double totalmarket=0;
    public double market;
    public double networth1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Stocks);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Stocks");
        TextView foot=(TextView) findViewById(R.id.footer);
        foot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent6 = new Intent();
                intent6.setAction(Intent.ACTION_VIEW);
                intent6.addCategory(Intent.CATEGORY_BROWSABLE);
                intent6.setData(Uri.parse("https://www.finnhub.io"));
                startActivity(intent6);
            }
        });;

        //portfolio
        SharedPreferences port = getSharedPreferences("Portfolio", MODE_PRIVATE);

        SharedPreferences money1 = getSharedPreferences("money", MODE_PRIVATE);
        SharedPreferences.Editor editor5 = money1.edit();

        if(money1.contains("initial")){
            money1.getString("initial","");
        }else{
            editor5.putString("initial","25000.00");
            editor5.apply();
            editor5.commit();
        }
        String money=money1.getString("initial","");
        TextView cashbalance= (TextView) findViewById(R.id.balance);
        cashbalance.setText("$"+money);

        TextView networth= (TextView) findViewById(R.id.worth);
        double cash=Double.parseDouble(money);

        //list{stock,shares,avgcost,totalcost,change,market}
        portlist= new ArrayList<>();
        //put key into a list
        Map<String, ?> prefsMap1 = port.getAll();
        for (Map.Entry<String, ?> entry: prefsMap1.entrySet()) {
            portlist.add(entry.getKey());
            Log.e("ii", String.valueOf(portlist));
        }

        ArrayList portArrayList=new ArrayList<>();
        for(int i=0;i<portlist.size();i++){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> list1 = gson.fromJson(port.getString(portlist.get(i), ""), type);
            Double price=Double.parseDouble(list1.get(6));
            Double avgprice=Double.parseDouble(list1.get(2));
            Double totalcost=Double.parseDouble(list1.get(3));
            Double share=Double.parseDouble(list1.get(1));

            market=price*share;
            totalmarket=totalmarket+market;

            Double change=(price-avgprice)*share;
            Double percent=(change/totalcost)*100;

            //change to string
            String ticker=list1.get(0);
            String share1=share+" shares"; //不要小数点
            String market1="$"+String.format("%.2f", market);

            String change1=String.format("%.2f", change);
            String percent1=String.format("%.2f", percent);

            String total="$"+change1+"("+percent1+"%"+")";
            portArrayList.add(new port(ticker, share1,market1,total));
        }
        portadapter Portadapter= new portadapter(this,portArrayList);
        portRV=findViewById(R.id.idRVport);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(Portadapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(portRV);

        //LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        portRV.setLayoutManager(new LinearLayoutManager(this));
        portRV.setAdapter(Portadapter);

        networth1=cash+totalmarket;
        networth.setText("$"+String.format("%.2f", networth1));

        //warchlist
        SharedPreferences sharedPreferences = getSharedPreferences("Local", MODE_PRIVATE);

        keylist= new ArrayList<>();
        Map<String, ?> prefsMap = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry: prefsMap.entrySet()) {
            keylist.add(entry.getKey());
            Log.v("SharedPreferences", entry.getKey());
        }
        Log.e("www", String.valueOf(keylist));

        watchArrayList=new ArrayList<>();
        for(int i=0;i<keylist.size();i++){
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> testlist = gson.fromJson(sharedPreferences.getString(keylist.get(i), ""), type);
            watchArrayList.add(new sharedwatchlist(testlist.get(0), testlist.get(1),testlist.get(2),testlist.get(3)));
        }

        Watchadapter= new watchadapter(this,watchArrayList);
        watchRV=findViewById(R.id.idRVwatchlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        watchRV.setLayoutManager(new LinearLayoutManager(this));
        watchRV.setAdapter(Watchadapter);

        enableSwipeToDeleteAndUndo();
    }



    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                SharedPreferences sharedPref = getSharedPreferences("Local",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                final sharedwatchlist item = Watchadapter.getData().get(position); //
                Watchadapter.removeItem(position);
                editor.remove(item.getTicker());
                editor.commit();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(watchRV);
    }


//set autocomplete adapter
    public class AutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {
    private List<String> mlistData;

    public AutoSuggestAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mlistData = new ArrayList<>();
    }

    public void setData(List<String> list) {
        mlistData.clear();
        mlistData.addAll(list);
    }

    @Override
    public int getCount() {
        return mlistData.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return mlistData.get(position);
    }

    /**
     * Used to Return the full object directly from adapter.
     *
     * @param position
     * @return
     */
    public String getObject(int position) {
        return mlistData.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = mlistData;
                    filterResults.count = mlistData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return dataFilter;
    }
}


    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;


    //autocomplete
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");


        // Get SearchView autocomplete object.
        final SearchAutoComplete searchAutoComplete;
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // Create a new ArrayAdapter and add data to search auto complete object.
        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        //searchAutoComplete.setText(autoSuggestAdapter.getObject(position));
                        String res=autoSuggestAdapter.getObject(position);
                        String searchsymbol= res.split(" ")[0];
                        searchAutoComplete.setText(searchsymbol);
                    }
                });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });
        //on submit search ticker to stockdetail
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent myIntent = new Intent(getApplicationContext(), stockdetail.class);
                myIntent.putExtra("ticker", query);
                startActivity(myIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    // api call
    public static class ApiCall {
        private static ApiCall mInstance;
        private RequestQueue mRequestQueue;
        private static Context mCtx;
        public ApiCall(Context ctx) {
            mCtx = ctx;
            mRequestQueue = getRequestQueue();
        }
        public static synchronized ApiCall getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new ApiCall(context);
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
        public static void make(Context ctx, String query, Response.Listener<String>
                listener, Response.ErrorListener errorListener) {
            String url1 = "https://mynewhw8.wn.r.appspot.com/searchutil/"+query;
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                    listener, errorListener);
            ApiCall.getInstance(ctx).addToRequestQueue(stringRequest1);
        }

    }

//select data
    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        String symbol=row.getString("symbol");
                        String description=row.getString("description");
                        String autoshow=symbol + " | "+ description;
                        stringList.add(autoshow);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
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
                    double dp= jsonObject.getDouble("dp");
                    double h= jsonObject.getDouble("h");
                    double l= jsonObject.getDouble("l");
                    double o= jsonObject.getDouble("o");
                    double pc= jsonObject.getDouble("pc");

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



}
