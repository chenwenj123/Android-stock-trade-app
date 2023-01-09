package com.example.stocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class sactivity1 extends Activity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Stocks);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sactivity1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bar);
        toolbar.setTitle("Stock");

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent0=new Intent(sactivity1.this,MainActivity.class);
                startActivity(intent0);
                finish();
            }
        },2000);

    }
}