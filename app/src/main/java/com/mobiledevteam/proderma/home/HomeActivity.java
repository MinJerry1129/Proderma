package com.mobiledevteam.proderma.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.MainActivity;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllClinicAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeClinicAdapter;
import com.mobiledevteam.proderma.cell.HomeProduct;
import com.mobiledevteam.proderma.cell.HomeProductAdapter;
import com.mobiledevteam.proderma.chat.ChatActivity;
import com.mobiledevteam.proderma.clinic.ClinicHomeActivity;
import com.mobiledevteam.proderma.clinic.ClinicNormalActivity;
import com.mobiledevteam.proderma.event.EventHomeActivity;
import com.mobiledevteam.proderma.history.HistoryHomeActivity;
import com.mobiledevteam.proderma.login.LoginHomeActivity;
import com.mobiledevteam.proderma.news.NewsActivity;
import com.mobiledevteam.proderma.result.ResultActivity;
import com.mobiledevteam.proderma.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements LocationListener {
    private RecyclerView _productRecycle;
    private RecyclerView _clinicRecycle;
    private TextView _txtAllNews;
    private TextView _txtAllProduct;
    private TextView _txtAllClinic;
    private TextView _txtNews;
    private ImageView _imgNoti;
    private ImageView _imgSetting;
    private ArrayList<HomeProduct> mProduct=new ArrayList<>();
    private ArrayList<HomeClinic> mClinic=new ArrayList<>();
    private String clinic_type = "normal";
    private String device_id;
    private String phone_token;
    private String loginCheck = "no 1 normal";
    private String mLoginStatus = "no";
    private LocationManager locationmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        device_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        phone_token = FirebaseInstanceId.getInstance().getToken();
        readFile();

        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);


        LinearLayoutManager layoutManager_product = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager_clinic = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _productRecycle = (RecyclerView)findViewById(R.id.recycler_product);
        _clinicRecycle = (RecyclerView)findViewById(R.id.recycler_clinic);
        _productRecycle.setLayoutManager(layoutManager_product);
        _clinicRecycle.setLayoutManager(layoutManager_clinic);
        clinic_type = Common.getInstance().getClinictype();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        _txtAllNews = (TextView)findViewById(R.id.txt_news_all);
        _txtAllProduct = (TextView)findViewById(R.id.txt_product_all);
        _txtAllClinic = (TextView)findViewById(R.id.txt_clinic_all);
        _txtNews = (TextView)findViewById(R.id.txt_news);
        _imgNoti = (ImageView) findViewById(R.id.img_notiview);
        _imgSetting = (ImageView) findViewById(R.id.img_setting);
        _imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSetting();
            }
        });
        _imgNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, NewsActivity.class);//LoginActivity.class);
                startActivity(intent);
            }
        });
        _txtAllClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, AllClinicActivity.class);//LoginActivity.class);
                startActivity(intent);
            }
        });
        _txtAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, AllProductActivity.class);//LoginActivity.class);
                startActivity(intent);
            }
        });
        getData();
        Log.d("deviceId:::", device_id);
        Log.d("phoneToken:::", phone_token);

    }
    private void readFile(){
        try {
            FileInputStream fileInputStream = openFileInput("loginstatus.pdm");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while((lines = bufferedReader.readLine()) != null){
                loginCheck = lines;
            }
            setValue();
        }catch (FileNotFoundException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setValue(){
        Log.d("login status:::", loginCheck);
        String[] parts = loginCheck.split(" ");
        if (parts[0].equals("no")){
            Log.d("login status:::", loginCheck);
            mLoginStatus = "no";
            Common.getInstance().setLogin_status("no");
        }else{
            Log.d("login yes:::", "yes");
            Log.d("login type:::", parts[2]);
            mLoginStatus = "yes";
            clinic_type = parts[2];
            Common.getInstance().setLogin_status("yes");
            Common.getInstance().setClinictype(clinic_type);
            Common.getInstance().setClinicID(parts[1]);
        }
    }
    private void initView(){
        HomeProductAdapter adapter_product = new HomeProductAdapter(getBaseContext(), mProduct);
        _productRecycle.setAdapter(adapter_product);
        HomeClinicAdapter adapter_clinic = new HomeClinicAdapter(getBaseContext(), mClinic);
        _clinicRecycle.setAdapter(adapter_clinic);
    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("deviceid", device_id);
        json.addProperty("phonetoken", phone_token);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getHomeData")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String selLang = Common.getInstance().getSelLang();
                                if(result.get("notification").isJsonNull()){

                                }else{
                                    JsonObject newsObject = result.getAsJsonObject("notification");
                                    if(newsObject != null){
                                        String news = newsObject.get("description").getAsString();
                                        if(selLang.equals("ar")){
                                            news = newsObject.get("descriptionar").getAsString();
                                        }
                                        _txtNews.setText(news);
                                    }
                                }

                                JsonArray clinics_array = result.get("clinicsInfo").getAsJsonArray();
                                for(JsonElement clinicElement : clinics_array){
                                    JsonObject theclinic = clinicElement.getAsJsonObject();
                                    String id = theclinic.get("id").getAsString();
                                    String name = theclinic.get("clinicname").getAsString();
                                    String location = theclinic.get("location").getAsString();
                                    String image = theclinic.get("photo").getAsString();
                                    String description = theclinic.get("information").getAsString();
                                    String phone = theclinic.get("mobile").getAsString();
                                    String whatsapp = theclinic.get("whatsapp").getAsString();
                                    String latitude = theclinic.get("latitude").getAsString();
                                    String longitude = theclinic.get("longitude").getAsString();
                                    LatLng clinic_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    String doctor = "0";
                                    mClinic.add(new HomeClinic(id,name,location,image,description,phone,whatsapp,doctor,clinic_location));
                                }
                                JsonArray products_array = result.get("productsInfo").getAsJsonArray();

                                for(JsonElement productElement : products_array){
                                    JsonObject theproduct = productElement.getAsJsonObject();
                                    String id = theproduct.get("id").getAsString();
                                    String brandid = theproduct.get("brandid").getAsString();
                                    String name = theproduct.get("name").getAsString();
                                    String price = theproduct.get("price").getAsString();
                                    String image = theproduct.get("photo").getAsString();
                                    String description = theproduct.get("information").getAsString();
                                    if(selLang.equals("ar")){
                                        name = theproduct.get("namear").getAsString();
                                        description = theproduct.get("informationar").getAsString();
                                    }
                                    mProduct.add(new HomeProduct(id,brandid,name,price,image,description));
                                }
                                initView();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    moveToClinic();
                    return true;
                case R.id.navigation_event:
                    moveToEvent();
                    return true;
                case R.id.navigation_chat:
                    moveToChat();
                    return true;
                case R.id.navigation_result:
                    moveToResult();
                    return true;
                case R.id.navigation_history:
                    moveToHistory();
                    return true;
            }
            return false;
        }
    };
    private void moveToClinic(){
        mLoginStatus = Common.getInstance().getLogin_status();
        if(mLoginStatus.equals("no")){
            Intent intent=new Intent(this, LoginHomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            if(clinic_type.equals("elite")){
                Intent intent=new Intent(this, ClinicHomeActivity.class).putExtra("clinic_id", Common.getInstance().getClinicID());
                startActivity(intent);
                finish();
            }else{
                Intent intent=new Intent(this, ClinicNormalActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
    private void moveToEvent(){
        Intent intent=new Intent(this, EventHomeActivity.class);
        startActivity(intent);
    }
    private void moveToChat(){
        Intent intent=new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
    private void moveToHistory(){
        mLoginStatus = Common.getInstance().getLogin_status();
        if(mLoginStatus.equals("no")){
            Intent intent=new Intent(this, LoginHomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this, HistoryHomeActivity.class);
            startActivity(intent);
        }

//        finish();
    }
    private void moveToSetting(){
        Intent intent=new Intent(this, SettingActivity.class);
        startActivity(intent);
        finish();
    }
    private void moveToResult(){
        Intent intent=new Intent(this, ResultActivity.class);
        startActivity(intent);
        finish();
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

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}