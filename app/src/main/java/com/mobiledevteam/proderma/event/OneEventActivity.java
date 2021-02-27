package com.mobiledevteam.proderma.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeProduct;
import com.mobiledevteam.proderma.cell.PageViewAdapter;
import com.mobiledevteam.proderma.home.PdfViewerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OneEventActivity extends AppCompatActivity {
    private ViewPager _productSlider;
    private TextView _eventTitle;
    private TextView _eventDateTime;
    private TextView _info;
    private TextView _location;
    private ImageView _eventPdf;
    private Button _btnRequeset;

    ArrayList<String> mAllEventList = new ArrayList<>();
    private int slideCurrentItem=0;
    private String mEventID;
    private String mPdfurl;
    private String login_status = "no";

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("selected:", String.valueOf(slideCurrentItem));
            _productSlider.setCurrentItem(slideCurrentItem);
            slideCurrentItem = slideCurrentItem + 1;
            if (mAllEventList.size()>slideCurrentItem){

            }else{
                slideCurrentItem = 0;
            }
            timerHandler.postDelayed(this, 5000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_event);
        mEventID = getIntent().getStringExtra("event_id");
        login_status = Common.getInstance().getLogin_status();
        _productSlider = (ViewPager)findViewById(R.id.slider_product);
        _eventTitle = (TextView)findViewById(R.id.txt_eventTitle);
        _eventDateTime = (TextView)findViewById(R.id.txt_eventdatetime);
        _info = (TextView)findViewById(R.id.txt_eventdescription);
        _location = (TextView)findViewById(R.id.txt_eventlocation);
        _btnRequeset=(Button)findViewById(R.id.btn_request);
        _eventPdf = (ImageView)findViewById(R.id.img_eventpdf);
        setReady();
        getData();
    }

    private void initView(){
        PageViewAdapter adapter = new PageViewAdapter(this, mAllEventList);
        _productSlider.setAdapter(adapter);
        timerHandler.postDelayed(timerRunnable, 0);
    }

    private void setReady(){
        if(login_status.equals("yes")){
            _btnRequeset.setVisibility(View.VISIBLE);
        }
        _eventPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PdfViewerActivity.class).putExtra("url", mPdfurl);
                startActivity(intent);
            }
        });
        _btnRequeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRequestEvent();
            }
        });
        _productSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                slideCurrentItem = position + 1;
                if (mAllEventList.size()>slideCurrentItem){

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
    private void onRequestEvent(){
        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = format.format(todaysdate);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("clinicid", Common.getInstance().getClinicID());
        json.addProperty("eventid", mEventID);
        json.addProperty("date",date);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/requestEvent")
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
                                    Toast.makeText(getBaseContext(),"Success", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(),"Fail", Toast.LENGTH_LONG).show();
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
        json.addProperty("id", mEventID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getEventInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject event_object = result.getAsJsonObject("eventInfo");
                                JsonArray event_images = result.get("eventImages").getAsJsonArray();
                                String id = event_object.get("id").getAsString();
                                String title = event_object.get("title").getAsString();
                                String edate = event_object.get("date").getAsString();
                                String etime = event_object.get("time").getAsString();
                                String location = event_object.get("location").getAsString();
                                String info = event_object.get("description").getAsString();
                                mPdfurl = Common.getInstance().getBaseURL() + event_object.get("pdf").getAsString();
                                _eventTitle.setText(title);
                                _eventDateTime.setText(edate + " "+etime);
                                _location.setText(location);
                                _info.setText(info);
                                for(JsonElement imageElement : event_images){
                                    JsonObject theimage = imageElement.getAsJsonObject();
                                    String image_id = theimage.get("id").getAsString();
                                    String image_url = theimage.get("url").getAsString();
                                    mAllEventList.add(Common.getInstance().getBaseURL() + image_url);
                                }
//                                Log.d("clinicinfo:::", mClinic.toString());
                                initView();
//                                sortList();

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