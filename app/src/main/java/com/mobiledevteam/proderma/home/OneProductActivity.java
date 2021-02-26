package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.ClinicDoctorAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeClinicAdapter;
import com.mobiledevteam.proderma.cell.HomeProduct;
import com.mobiledevteam.proderma.cell.HomeProductAdapter;
import com.mobiledevteam.proderma.cell.PageViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OneProductActivity extends AppCompatActivity {
    private ViewPager _productSlider;
    private TextView _productname;
    private TextView _price;
    private TextView _info;
    private TextView _imgInfo;
    private Button _btnRequeset;
    private EditText _Count;
    private TextView _Extra;
    private RecyclerView _clinicRecycle;
    private LinearLayout price_view;

    private HomeProduct mOneProduct;
    ArrayList<String> mAllProductList = new ArrayList<>();
    private ArrayList<HomeClinic> mClinic=new ArrayList<>();
    private String mProductID;
    private int slideCurrentItem=0;
    private String mPercent;
    private String login_status = "no";

    private Location mCurrentLocation;
    private Location mClinicLocation1;
    private Location mClinicLocation2;
    private LatLng my_location;
    private LocationManager locationmanager;


    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("selected:", String.valueOf(slideCurrentItem));
            _productSlider.setCurrentItem(slideCurrentItem);
            slideCurrentItem = slideCurrentItem + 1;
            if (mAllProductList.size()>slideCurrentItem){

            }else{
                slideCurrentItem = 0;
            }
            timerHandler.postDelayed(this, 5000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_product);
        mProductID = getIntent().getStringExtra("product_id");
        login_status = Common.getInstance().getLogin_status();
        _productSlider = (ViewPager)findViewById(R.id.slider_product);
        _productname = (TextView)findViewById(R.id.txt_productname);
        _price = (TextView)findViewById(R.id.txt_productprice);
        _info = (TextView)findViewById(R.id.txt_productdescription);
        _Count = (EditText) findViewById(R.id.txt_buycount);
        _Extra = (TextView) findViewById(R.id.txt_extracount);
        _imgInfo = (TextView)findViewById(R.id.txt_info);
        _btnRequeset=(Button)findViewById(R.id.btn_request);
        _clinicRecycle = (RecyclerView)findViewById(R.id.recycler_clinic);
        price_view = (LinearLayout) findViewById(R.id.price_view);
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

        setReady();
        getData();
    }
    private void setReady(){
        LinearLayoutManager layoutManager_clinic = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _clinicRecycle.setLayoutManager(layoutManager_clinic);
        if(login_status.equals("yes")){
            _btnRequeset.setVisibility(View.VISIBLE);
            price_view.setVisibility(View.VISIBLE);
        }
        _btnRequeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRequest();
            }
        });
        _imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerHandler.removeCallbacks(timerRunnable);
                Intent intent = new Intent(getApplicationContext(), TrainingActivity.class).putExtra("product_id", mProductID);
                startActivity(intent);
                finish();
            }
        });
        _Count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int extra = 0;
                int count = 0;
                if(_Count.getText().toString().equals("")){
                    count = 0;
                }else{
                    count = Integer.parseInt(_Count.getText().toString());
                }
                int percent = Integer.parseInt(mPercent);

                if(Common.getInstance().getClinictype().equals("elite")){
                    extra = (count * percent) / 100;
                    _Extra.setText(String.valueOf(extra));

                }


            }
        });
        _productSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                slideCurrentItem = position + 1;
                if (mAllProductList.size()>slideCurrentItem){

                }else{
                    slideCurrentItem = 0;
                }
                timerHandler.removeCallbacks(timerRunnable);
                timerHandler.postDelayed(timerRunnable, 5000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void initclinicView(){
        HomeClinicAdapter adapter_clinic = new HomeClinicAdapter(getBaseContext(), mClinic);
        _clinicRecycle.setAdapter(adapter_clinic);
    }
    private void initView(){
        setInfo();
        PageViewAdapter adapter = new PageViewAdapter(this, mAllProductList);
        _productSlider.setAdapter(adapter);
        timerHandler.postDelayed(timerRunnable, 0);
    }
    private void setInfo(){
        _productname.setText(mOneProduct.getmName());
        _price.setText(mOneProduct.getmPrice());
        _info.setText(mOneProduct.getmDescription());
    }
    private void productRequest(){
        if(!validate()){
            return;
        }
        if(Common.getInstance().getLogin_status().equals("no")){
            Toast.makeText(getBaseContext(), "Please Login Or Contact to Support Team", Toast.LENGTH_LONG).show();
            return;
        }
        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = format.format(todaysdate);
        String extra = "";
        if(Common.getInstance().getClinictype().equals("elite")){
            extra = _Extra.getText().toString();
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("clinicid", Common.getInstance().getClinicID());
        json.addProperty("productid", mProductID);
        json.addProperty("count",_Count.getText().toString());
        json.addProperty("extra",extra);
        json.addProperty("date",date);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/requestOrder")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String order_result = result.get("result").getAsString();
                                if(order_result.equals("ok")){
                                    Toast.makeText(getBaseContext(),"Request order product success", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(),"Request order product Fail", Toast.LENGTH_LONG).show();
                                }

                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mProductID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getProductInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject product_object = result.getAsJsonObject("productInfo");
                                JsonArray product_images = result.get("productImages").getAsJsonArray();
                                JsonArray clinics_array = result.get("productOrder").getAsJsonArray();
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

                                String id = product_object.get("id").getAsString();
                                String name = product_object.get("name").getAsString();
                                String price = product_object.get("price").getAsString();
                                String info = product_object.get("information").getAsString();
                                String image = product_object.get("photo").getAsString();
                                mPercent = product_object.get("percent").getAsString();
                                mOneProduct = new HomeProduct(id,name,price,image,info);
                                for(JsonElement imageElement : product_images){
                                    JsonObject theimage = imageElement.getAsJsonObject();
                                    String image_id = theimage.get("id").getAsString();
                                    String image_url = theimage.get("url").getAsString();
                                    mAllProductList.add(Common.getInstance().getBaseURL() + image_url);
                                }
                                Log.d("clinicinfo:::", mClinic.toString());
                                initView();
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
        if(mClinic.size() != 0){
            for (int i =0; i< mClinic.size(); i++){
                for(int j= i+1; j<mClinic.size(); j++){
                    HomeClinic temp;
                    mClinicLocation1.setLatitude(mClinic.get(i).getmLatLng().latitude);
                    mClinicLocation1.setLongitude(mClinic.get(i).getmLatLng().longitude);
                    mClinicLocation2.setLatitude(mClinic.get(j).getmLatLng().latitude);
                    mClinicLocation2.setLongitude(mClinic.get(j).getmLatLng().longitude);
                    if(mCurrentLocation.distanceTo(mClinicLocation1) > mCurrentLocation.distanceTo(mClinicLocation2)){
                        temp = mClinic.get(i);
                        mClinic.set(i, mClinic.get(j));
                        mClinic.set(j, temp);
                    }
                }
            }
        }
        initclinicView();
    }

    public boolean validate() {
        boolean valid = true;
        String count = _Count.getText().toString();

        if (count.isEmpty()) {
            _Count.setError("Input Count");
            valid = false;
        } else {
            int product_count = Integer.parseInt(count);
            if(product_count < 1){
                _Count.setError("Input Count");
                valid = false;
            }else{
                _Count.setError(null);
            }
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        timerHandler.removeCallbacks(timerRunnable);
        super.onBackPressed();
    }
}
