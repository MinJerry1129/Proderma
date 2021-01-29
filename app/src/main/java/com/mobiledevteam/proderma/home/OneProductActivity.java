package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
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
import com.mobiledevteam.proderma.cell.HomeProduct;
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
    private ImageView _imgInfo;
    private Button _btnRequeset;
    private EditText _Count;

    private HomeProduct mOneProduct;
    ArrayList<String> mAllProductList = new ArrayList<>();
    private String mProductID;
    private int slideCurrentItem=0;


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
        _productSlider = (ViewPager)findViewById(R.id.slider_product);
        _productname = (TextView)findViewById(R.id.txt_productname);
        _price = (TextView)findViewById(R.id.txt_productprice);
        _info = (TextView)findViewById(R.id.txt_productdescription);
        _Count = (EditText) findViewById(R.id.txt_buycount);
        _imgInfo = (ImageView)findViewById(R.id.img_info);
        _btnRequeset=(Button)findViewById(R.id.btn_request);
        setReady();
        getData();
    }
    private void setReady(){
        _btnRequeset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productRequest();
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
        int extra = 0;
        int count = Integer.parseInt(_Count.getText().toString());
        if(Common.getInstance().getClinictype().equals("elite")){
            extra = count / 20;
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
        json.addProperty("extra",String.valueOf(extra));
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
                                String id = product_object.get("id").getAsString();
                                String name = product_object.get("name").getAsString();
                                String price = product_object.get("price").getAsString();
                                String info = product_object.get("information").getAsString();
                                String image = product_object.get("photo").getAsString();
                                mOneProduct = new HomeProduct(id,name,price,image,info);
                                for(JsonElement imageElement : product_images){
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
