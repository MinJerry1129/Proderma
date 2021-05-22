package com.mobiledevteam.proderma.result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.HomeActivity;

public class ResultActivity extends AppCompatActivity {
    private GridView _allGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        _allGridView = (GridView)findViewById(R.id.grid_result);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}