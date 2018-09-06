package com.example.youbi.myapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoInternetActivity extends AppCompatActivity {

    private Context context;
    private Button refresh_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        context = getApplicationContext();
        refresh_btn = findViewById(R.id.refreshBtn);

    }

    public void onClick(View v) {
        Intent i = new Intent(context, MainActivity.class);
        startActivity(i);
    }
}
