package com.example.stocks;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class sharedwatchlist {

    private String ticker;
    private String name;
    private String price;
    private String total;


    // Constructor
    public sharedwatchlist(String ticker, String name, String price, String total) {
        this.ticker = ticker;
        this.name = name;
        this.price=price;
        this.total=total;


    }
    // Getter and Setter
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }



}
