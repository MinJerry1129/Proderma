package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllImageAdapter;
import com.mobiledevteam.proderma.cell.AllProductAdapter;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.ClinicDoctorAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.ImageSliderPhoto;
import com.mobiledevteam.proderma.cell.PageViewAdapter;
import com.mobiledevteam.proderma.home.OneProductActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class ClinicImageActivity extends AppCompatActivity {
    private GridView _allGridView;
    private String mClinicID;
    private ArrayList<ImageSliderPhoto> mAllImageList = new ArrayList<>();
    private Image image;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_image);
        mClinicID = Common.getInstance().getClinicID();
        _allGridView = (GridView)findViewById(R.id.grid_allClinicImages);
        mAllImageList.add(new ImageSliderPhoto("0","",""));
        setReady();
        getData();
    }
    private void setReady(){
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    onPickImage();
                }else{
                    Intent intent = new Intent(getBaseContext(), ClinicImageViewActivity.class)
                            .putExtra("imageid", mAllImageList.get(position).getmID())
                            .putExtra("imageurl", mAllImageList.get(position).getmUrl());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public void onPickImage() {
        ImagePicker.Companion.with(this).saveDir(Environment.getExternalStorageDirectory()).cropSquare().maxResultSize(700,700).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            filePath = ImagePicker.Companion.getFilePath(data);
            addImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void addImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String clinicImage = encodedImage;

        JsonObject json = new JsonObject();
        json.addProperty("id", mClinicID);
        json.addProperty("photo", clinicImage);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/addClinicImage")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String status = result.get("status").getAsString();
                                if(status.equals("ok")){
                                    Intent refresh = new Intent(getBaseContext(), ClinicImageActivity.class);
                                    startActivity(refresh);//Start the same Activity
                                    finish();
                                }else{
                                    Toast.makeText(getBaseContext(),"Add image failed.", Toast.LENGTH_LONG).show();
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
        json.addProperty("id", mClinicID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getClinicImage")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonArray clinics_images = result.get("clinicImages").getAsJsonArray();
                                for(JsonElement imageElement : clinics_images){
                                    JsonObject theimage = imageElement.getAsJsonObject();
                                    String image_id = theimage.get("id").getAsString();
                                    String image_status = theimage.get("status").getAsString();
                                    String image_url = theimage.get("url").getAsString();
                                    mAllImageList.add(new ImageSliderPhoto(image_id,image_status,image_url));
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
    private void initView(){
        AllImageAdapter adapter_clinic = new AllImageAdapter(getBaseContext(), mAllImageList);
        _allGridView.setAdapter(adapter_clinic);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ClinicImageActivity.this, ClinicHomeActivity.class);
        startActivity(intent);
        finish();
    }
}