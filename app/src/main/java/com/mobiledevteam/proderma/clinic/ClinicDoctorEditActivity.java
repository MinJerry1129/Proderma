package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.HomeClinic;

public class ClinicDoctorEditActivity extends AppCompatActivity {
    private ImageView _imgDoctor;
    private EditText _docName;
    private EditText _docAge;
    private EditText _docInfo;
    private Button _btnUpdate;
    private Button _btnRemove;
    private TextView _txtStatus;
    private String mDoctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_doctor_edit);
        mDoctorID = getIntent().getStringExtra("doctor_id");
        _imgDoctor = (ImageView)findViewById(R.id.img_doctor);
        _txtStatus = (TextView) findViewById(R.id.txt_approveStatus);
        _docName = (EditText) findViewById(R.id.input_doctorname);
        _docAge = (EditText) findViewById(R.id.input_doctorname);
        _docInfo = (EditText) findViewById(R.id.input_doctorname);
        _btnUpdate = (Button) findViewById(R.id.btn_add);
        _btnRemove = (Button) findViewById(R.id.btn_delete);
        setReady();
        getData();
    }
    private void setReady(){

    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mDoctorID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getDoctorInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
//                                JsonObject clinics_object = result.getAsJsonObject("clinicInfo");
//                                JsonArray doctors_array = result.get("doctorsInfo").getAsJsonArray();
//                                JsonArray clinics_images = result.get("clinicImages").getAsJsonArray();
//                                String id = clinics_object.get("id").getAsString();
//                                String name = clinics_object.get("clinicname").getAsString();
//                                String location = clinics_object.get("location").getAsString();
//                                String image = clinics_object.get("photo").getAsString();
//                                String description = clinics_object.get("information").getAsString();
//                                String phone = clinics_object.get("mobile").getAsString();
//                                String doctor = "0";
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
        Intent intent=new Intent(ClinicDoctorEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }
}