package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mobiledevteam.proderma.R;

public class ClinicDoctorEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_doctor_edit);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ClinicDoctorEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }
}