package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.HomeActivity;

public class ClinicNormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_normal);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}