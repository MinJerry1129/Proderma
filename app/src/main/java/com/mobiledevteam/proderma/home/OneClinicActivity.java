package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
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

import java.util.ArrayList;

public class OneClinicActivity extends AppCompatActivity {
    private ViewPager _clinicSlider;
    private RecyclerView _doctorRecycle;
    private TextView _clinicname;
    private TextView _cliniclocation;
    private TextView _clinicphone;
    private TextView _clinicdescription;
    private ImageView _clinicimg;

    private ArrayList<ClinicDoctor> mDoctor=new ArrayList<>();
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
        setContentView(R.layout.activity_one_clinic);
        mClinicID = getIntent().getStringExtra("clinic_id");
        _clinicSlider = (ViewPager) findViewById(R.id.slider_clinic);
        _doctorRecycle = (RecyclerView)findViewById(R.id.recycler_doctor);
        _clinicname = (TextView)findViewById(R.id.txt_clinicname);
        _cliniclocation = (TextView)findViewById(R.id.txt_cliniclocation);
        _clinicphone = (TextView)findViewById(R.id.txt_phone);
        _clinicdescription = (TextView)findViewById(R.id.txt_description);
        _clinicimg = (ImageView) findViewById(R.id.img_clinic);
        Common.getInstance().setClinicpagetype("preview");
        setReady();
        getData();
    }
    private void setReady(){
        LinearLayoutManager layoutManager_doctor = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        _doctorRecycle.setLayoutManager(layoutManager_doctor);
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
        ClinicDoctorAdapter adapter_doctor = new ClinicDoctorAdapter(getBaseContext(), mDoctor);
        _doctorRecycle.setAdapter(adapter_doctor);
        PageViewAdapter adapter = new PageViewAdapter(this, mAllProductList);
        _clinicSlider.setAdapter(adapter);
        timerHandler.postDelayed(timerRunnable, 0);
    }
    private void setInfo(){
        _clinicname.setText(mOneClinic.getmName());
        _cliniclocation.setText(mOneClinic.getmLocation());
        _clinicphone.setText(mOneClinic.getmPhone());
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
                                JsonArray clinics_images = result.get("clinicImages").getAsJsonArray();
                                String id = clinics_object.get("id").getAsString();
                                String name = clinics_object.get("clinicname").getAsString();
                                String location = clinics_object.get("location").getAsString();
                                String image = clinics_object.get("photo").getAsString();
                                String description = clinics_object.get("information").getAsString();
                                String phone = clinics_object.get("mobile").getAsString();
                                String doctor = "0";
                                mOneClinic = new HomeClinic(id,name,location,image,description,phone,doctor);

                                for(JsonElement doctorElement : doctors_array){
                                    JsonObject theDoctor = doctorElement.getAsJsonObject();
                                    String doctor_id = theDoctor.get("id").getAsString();
                                    String doctor_name = theDoctor.get("name").getAsString();
                                    String doctor_age = theDoctor.get("age").getAsString();
                                    String doctor_info = theDoctor.get("information").getAsString();
                                    String doctor_status = theDoctor.get("status").getAsString();
                                    String doctor_image = Common.getInstance().getBaseURL() + theDoctor.get("photo").getAsString();
                                    if(doctor_status.equals("enable")){
                                        mDoctor.add(new ClinicDoctor(doctor_id, doctor_name, doctor_age, doctor_info, doctor_image,doctor_status));
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
        timerHandler.removeCallbacks(timerRunnable);
        super.onBackPressed();
    }
}