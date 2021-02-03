package com.mobiledevteam.proderma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mobiledevteam.proderma.home.HomeActivity;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements LocationListener {
    private Button _btnEnglish;
    private Button _btnArabic;
    private LocationManager locationmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        _btnEnglish = (Button)findViewById(R.id.btn_english);
        _btnArabic = (Button)findViewById(R.id.btn_arabic);
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
        ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        _btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                final Configuration configuration = getResources().getConfiguration();
                LocaleHelper.setLocale(getBaseContext(), "en");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLayoutDirection(new Locale("en"));
                }
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                Common.getInstance().setSelLang("en");
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
            }
        });
        _btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                final Configuration configuration = getResources().getConfiguration();
                LocaleHelper.setLocale(getBaseContext(), "ar");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    configuration.setLayoutDirection(new Locale("ar"));
                }
                getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                Common.getInstance().setSelLang("ar");
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },3000);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location:::", String.valueOf( location.getLatitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("providerEnabled:", s);
    }

    @Override
    public void onProviderDisabled(String s) {

    }
}