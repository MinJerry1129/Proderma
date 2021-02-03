package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.mobiledevteam.proderma.cell.AllImageAdapter;
import com.mobiledevteam.proderma.cell.AllProductAdapter;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.ClinicDoctorAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.ImageSliderPhoto;
import com.mobiledevteam.proderma.cell.PageViewAdapter;

import java.util.ArrayList;

public class ClinicImageActivity extends AppCompatActivity {
    private GridView _allGridView;
    private String mClinicID;
    private ArrayList<ImageSliderPhoto> mAllImageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_image);
        mClinicID = Common.getInstance().getClinicID();
        _allGridView = (GridView)findViewById(R.id.grid_allClinicImages);
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