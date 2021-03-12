package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.LanguageActivity;
import com.mobiledevteam.proderma.MainActivity;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.ClinicDoctorAdapter;
import com.mobiledevteam.proderma.cell.ClinicOffer;
import com.mobiledevteam.proderma.cell.ClinicOfferAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.ImageSliderPhoto;
import com.mobiledevteam.proderma.cell.PageViewAdapter;
import com.mobiledevteam.proderma.home.HomeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClinicHomeActivity extends AppCompatActivity {
    private ViewPager _clinicSlider;
    private RecyclerView _doctorRecycle;
    private GridView _offerGridView;
    private TextView _clinicname;
    private TextView _cliniclocation;
    private TextView _clinicphone;
    private TextView _clinicwhatsapp;
    private TextView _clinicdescription;
    private ImageView _clinicimg;
    private ImageView _imgPhoto;
    private ImageView _imgInfo;
    private ImageView _imgDoctor;
    private ImageView _imgOffer;
    private ArrayList<ClinicDoctor> mDoctor=new ArrayList<>();
    private ArrayList<ClinicOffer> mOffer=new ArrayList<>();
    private ArrayList<String> mAllProductList = new ArrayList<>();
    private String mClinicID;
    private HomeClinic mOneClinic;
    private int slideCurrentItem=0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("selected:", String.valueOf(slideCurrentItem));
            _clinicSlider.setCurrentItem(slideCurrentItem);
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
        setContentView(R.layout.activity_clinic_home);
        mClinicID = Common.getInstance().getClinicID();
        _clinicSlider = (ViewPager)findViewById(R.id.slider_clinic);
        _doctorRecycle = (RecyclerView)findViewById(R.id.recycler_doctor);
        _offerGridView = (GridView)findViewById(R.id.grid_allOffer);
        _imgPhoto = (ImageView) findViewById(R.id.img_editphoto);
        _imgInfo = (ImageView) findViewById(R.id.img_editpinfo);
        _imgDoctor = (ImageView) findViewById(R.id.img_editdoctor);
        _imgOffer = (ImageView) findViewById(R.id.img_editOffer);
        _clinicname = (TextView)findViewById(R.id.txt_clinicname);
        _cliniclocation = (TextView)findViewById(R.id.txt_cliniclocation);
        _clinicphone = (TextView)findViewById(R.id.txt_phone);
        _clinicwhatsapp = (TextView)findViewById(R.id.txt_whatsapp);
        _clinicdescription = (TextView)findViewById(R.id.txt_description);
        _clinicimg = (ImageView) findViewById(R.id.img_clinic);

        Common.getInstance().setClinicpagetype("home");
        setReady();
        getData();
    }
    private void setReady(){
        LinearLayoutManager layoutManager_doctor = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _doctorRecycle.setLayoutManager(layoutManager_doctor);
        _doctorRecycle.setVisibility(View.GONE);
        _offerGridView.setVisibility(View.GONE);
        _imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                timerHandler.removeCallbacks(timerRunnable);
                Intent intent=new Intent(ClinicHomeActivity.this, ClinicImageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                timerHandler.removeCallbacks(timerRunnable);
                Intent intent=new Intent(ClinicHomeActivity.this, ClinicInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                timerHandler.removeCallbacks(timerRunnable);
                Intent intent=new Intent(ClinicHomeActivity.this, ClinicDoctorAddActivity.class);//LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _imgOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                timerHandler.removeCallbacks(timerRunnable);
                Intent intent=new Intent(ClinicHomeActivity.this, ClinicOfferAddActivity.class);//LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _clinicSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("selecteditem:", String.valueOf(position));
            }
            @Override
            public void onPageSelected(int position) {
                Log.d("selectedpoist:", String.valueOf(position));
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
    private void initView(){
        setInfo();
        if(mDoctor.size()>0){
            _doctorRecycle.setVisibility(View.VISIBLE);
        }
        if(mOffer.size()>0){
            _offerGridView.setVisibility(View.VISIBLE);
        }
        ClinicDoctorAdapter adapter_doctor = new ClinicDoctorAdapter(getBaseContext(), mDoctor);
        _doctorRecycle.setAdapter(adapter_doctor);
        PageViewAdapter adapter = new PageViewAdapter(this, mAllProductList);
        _clinicSlider.setAdapter(adapter);
        ClinicOfferAdapter adapter_clinic = new ClinicOfferAdapter(getBaseContext(), mOffer);
        _offerGridView.setAdapter(adapter_clinic);
        timerHandler.postDelayed(timerRunnable, 0);
    }
    private void setInfo(){
        _clinicname.setText(mOneClinic.getmName());
        _cliniclocation.setText(mOneClinic.getmLocation());
        _clinicphone.setText(mOneClinic.getmPhone());
        _clinicwhatsapp.setText(mOneClinic.getmWhatsapp());
        _clinicdescription.setText(mOneClinic.getmDescription());
        Ion.with(getBaseContext()).load(mOneClinic.getmImage()).intoImageView(_clinicimg);
    }

    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mClinicID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getClinicInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject clinics_object = result.getAsJsonObject("clinicInfo");
                                JsonArray doctors_array = result.get("doctorsInfo").getAsJsonArray();
                                JsonArray offers_array = result.get("clinicOffer").getAsJsonArray();
                                JsonArray clinics_images = result.get("clinicImages").getAsJsonArray();
                                String id = clinics_object.get("id").getAsString();
                                String name = clinics_object.get("clinicname").getAsString();
                                String location = clinics_object.get("location").getAsString();
                                String image = clinics_object.get("photo").getAsString();
                                String description = clinics_object.get("information").getAsString();
                                String phone = clinics_object.get("mobile").getAsString();
                                String whatsapp = clinics_object.get("whatsapp").getAsString();
                                String latitude = clinics_object.get("latitude").getAsString();
                                String longitude = clinics_object.get("longitude").getAsString();
                                LatLng clinic_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                String doctor = "0";
                                mOneClinic = new HomeClinic(id,name,location,image,description,phone,whatsapp,doctor,clinic_location);

                                for(JsonElement doctorElement : doctors_array){
                                    JsonObject theDoctor = doctorElement.getAsJsonObject();
                                    String doctor_id = theDoctor.get("id").getAsString();
                                    String doctor_name = theDoctor.get("name").getAsString();
                                    String doctor_age = theDoctor.get("age").getAsString();
                                    String doctor_info = theDoctor.get("information").getAsString();
                                    String status = theDoctor.get("status").getAsString();
                                    String doctor_image = Common.getInstance().getBaseURL() + theDoctor.get("photo").getAsString();

                                    mDoctor.add(new ClinicDoctor(doctor_id, doctor_name, doctor_age, doctor_info, doctor_image,status));
                                }
                                for(JsonElement offerElement : offers_array){
                                    JsonObject theOffer = offerElement.getAsJsonObject();
                                    String offer_id = theOffer.get("id").getAsString();
                                    String offer_title = theOffer.get("title").getAsString();
                                    String offer_info = theOffer.get("description").getAsString();
                                    String offer_status = theOffer.get("status").getAsString();
                                    if(offer_status.equals("enable")){
                                        mOffer.add(new ClinicOffer(offer_id, offer_title, offer_info, offer_status));
                                    }
                                }
                                for(JsonElement imageElement : clinics_images){
                                    JsonObject theimage = imageElement.getAsJsonObject();
                                    String image_id = theimage.get("id").getAsString();
                                    String image_url = theimage.get("url").getAsString();
                                    mAllProductList.add(Common.getInstance().getBaseURL() + image_url);
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
        Intent intent=new Intent(ClinicHomeActivity.this, HomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerHandler.removeCallbacks(timerRunnable);
    }
}