package com.mobiledevteam.proderma.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.mobiledevteam.proderma.setting.SettingActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView _productRecycle;
    private RecyclerView _clinicRecycle;
    private TextView _txtAllNews;
    private TextView _txtAllProduct;
    private TextView _txtAllClinic;
    private ArrayList<HomeProduct> mProduct=new ArrayList<>();
    private ArrayList<HomeClinic> mClinic=new ArrayList<>();
    private String clinic_type = "normal";
    private String device_id;
    private String phone_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        device_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        phone_token = FirebaseInstanceId.getInstance().getToken();

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
                                JsonArray clinics_array = result.get("clinicsInfo").getAsJsonArray();
                                for(JsonElement clinicElement : clinics_array){
                                    JsonObject theclinic = clinicElement.getAsJsonObject();
                                    String id = theclinic.get("id").getAsString();
                                    String name = theclinic.get("clinicname").getAsString();
                                    String location = theclinic.get("location").getAsString();
                                    String image = theclinic.get("photo").getAsString();
                                    String description = theclinic.get("information").getAsString();
                                    String phone = theclinic.get("mobile").getAsString();
                                    String latitude = theclinic.get("latitude").getAsString();
                                    String longitude = theclinic.get("longitude").getAsString();
                                    LatLng clinic_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    String doctor = "0";
                                    mClinic.add(new HomeClinic(id,name,location,image,description,phone,doctor,clinic_location));
                                }
                                JsonArray products_array = result.get("productsInfo").getAsJsonArray();
                                for(JsonElement productElement : products_array){
                                    JsonObject theproduct = productElement.getAsJsonObject();
                                    String id = theproduct.get("id").getAsString();
                                    String name = theproduct.get("name").getAsString();
                                    String price = theproduct.get("price").getAsString();
                                    String image = theproduct.get("photo").getAsString();
                                    String description = theproduct.get("information").getAsString();
                                    mProduct.add(new HomeProduct(id,name,price,image,description));
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
                case R.id.navigation_history:
                    moveToHistory();
                    return true;
                case R.id.navigation_setting:
                    moveToSetting();
                    return true;
            }
            return false;
        }
    };
    private void moveToClinic(){
        String mLoginStatus = Common.getInstance().getLogin_status();
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
        String mLoginStatus = Common.getInstance().getLogin_status();
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
}