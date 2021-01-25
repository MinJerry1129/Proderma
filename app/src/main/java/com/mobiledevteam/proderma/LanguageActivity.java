package com.mobiledevteam.proderma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.mobiledevteam.proderma.home.HomeActivity;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private Button _btnEnglish;
    private Button _btnArabic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        _btnEnglish = (Button)findViewById(R.id.btn_english);
        _btnArabic = (Button)findViewById(R.id.btn_arabic);
        _btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Configuration configuration = getResources().getConfiguration();
                LocaleHelper.setLocale(getBaseContext(), "en");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLayoutDirection(new Locale("en"));
                }
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                Common.getInstance().setSelLang("en");
                Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Configuration configuration = getResources().getConfiguration();
                LocaleHelper.setLocale(getBaseContext(), "en");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLayoutDirection(new Locale("en"));
                }
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                Common.getInstance().setSelLang("ar");
                Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}