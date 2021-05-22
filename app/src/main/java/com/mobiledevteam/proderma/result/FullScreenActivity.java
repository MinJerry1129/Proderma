package com.mobiledevteam.proderma.result;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

public class FullScreenActivity extends AppCompatActivity {
    PhotoView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        imageview =(PhotoView) findViewById(R.id.photoView);
        Glide.with(this)
                .load(Common.getInstance().getImgUrl())
                .into(imageview);
    }
}