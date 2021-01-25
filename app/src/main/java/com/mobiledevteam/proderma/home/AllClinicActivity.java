package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_clinic);
        _allGridView = (GridView)findViewById(R.id.grid_allClinic);
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
                                    String doctor = "0";
                                    mAllClinicList.add(new HomeClinic(id,name,location,image,description,phone,doctor));
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
        AllClinicAdapter adapter_clinic = new AllClinicAdapter(getBaseContext(), mAllClinicList);
        _allGridView.setAdapter(adapter_clinic);
    }
}