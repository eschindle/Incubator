package com.mycompany.incubatoralpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void loginClick(View v){
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
        finish();
    }

    public void signUpClick(View v){
        startActivity(new Intent(StartActivity.this, SignUpActivty.class));
        finish();
    }
}