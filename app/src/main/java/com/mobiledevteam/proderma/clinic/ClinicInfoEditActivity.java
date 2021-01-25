package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mobiledevteam.proderma.LanguageActivity;
import com.mobiledevteam.proderma.MainActivity;
import com.mobiledevteam.proderma.R;

public class ClinicInfoEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_info_edit);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ClinicInfoEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }
}