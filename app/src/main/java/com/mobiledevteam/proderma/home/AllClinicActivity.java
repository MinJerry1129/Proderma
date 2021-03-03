package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllClinicAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeClinicAdapter;
import com.mobiledevteam.proderma.cell.HomeProduct;

import java.util.ArrayList;

public class AllClinicActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<HomeClinic> mAllClinicList = new ArrayList<>();
    private Location mCurrentLocation;
    private Location mClinicLocation1;
    private Location mClinicLocation2;
    private LatLng my_location;
    private LocationManager locationmanager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clinic);
        _allGridView = (GridView)findViewById(R.id.grid_allClinic);
        mCurrentLocation = new Location("current");
        mClinicLocation1 = new Location("clinic1");
        mClinicLocation2 = new Location("clinic2");
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(new Criteria(), true);
        Location LocationGps;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            LocationGps = locationmanager.getLastKnownLocation(provider);
        }
        if (LocationGps != null) {
            my_location = new LatLng(LocationGps.getLatitude(), LocationGps.getLongitude());
        }else{
            my_location = new LatLng(48.8499,2.3512);
        }
        mCurrentLocation.setLatitude(my_location.latitude);
        mCurrentLocation.setLongitude(my_location.longitude);

        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), OneClinicActivity.class).putExtra("clinic_id", mAllClinicList.get(position).getmId());
                startActivity(intent);
            }
        });
        getData();
    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("email", "");
        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getClinicsInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
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
                                    String whatsapp = theclinic.get("whatsapp").getAsString();
                                    String latitude = theclinic.get("latitude").getAsString();
                                    String longitude = theclinic.get("longitude").getAsString();
                                    LatLng clinic_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                    String doctor = "0";
                                    mAllClinicList.add(new HomeClinic(id,name,location,image,description,phone,whatsapp,doctor,clinic_location));
                                }
                                sortList();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void sortList(){
        if(mAllClinicList.size() != 0){
            for (int i =0; i< mAllClinicList.size(); i++){
                for(int j= i+1; j<mAllClinicList.size(); j++){
                    HomeClinic temp;
                    mClinicLocation1.setLatitude(mAllClinicList.get(i).getmLatLng().latitude);
                    mClinicLocation1.setLongitude(mAllClinicList.get(i).getmLatLng().longitude);
                    mClinicLocation2.setLatitude(mAllClinicList.get(j).getmLatLng().latitude);
                    mClinicLocation2.setLongitude(mAllClinicList.get(j).getmLatLng().longitude);
                    if(mCurrentLocation.distanceTo(mClinicLocation1) > mCurrentLocation.distanceTo(mClinicLocation2)){
                        temp = mAllClinicList.get(i);
                        mAllClinicList.set(i, mAllClinicList.get(j));
                        mAllClinicList.set(j, temp);
                    }
                }
            }
        }
        initView();
    }
    private void initView(){
        AllClinicAdapter adapter_clinic = new AllClinicAdapter(getBaseContext(), mAllClinicList);
        _allGridView.setAdapter(adapter_clinic);
    }
}