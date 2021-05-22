package com.mobiledevteam.proderma.result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.HomeActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}