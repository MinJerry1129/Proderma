package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.HomeActivity;

import java.io.ByteArrayOutputStream;

public class ClinicNormalActivity extends AppCompatActivity {
    private EditText _firstname;
    private EditText _secondname;
    private EditText _clinicname;
    private EditText _phone;
    private Button _update;
    private String mClinicID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_normal);
        mClinicID = Common.getInstance().getClinicID();
        _firstname = (EditText)findViewById(R.id.input_firstname);
        _secondname = (EditText)findViewById(R.id.input_secondname);
        _clinicname = (EditText)findViewById(R.id.input_clinicname);
        _phone = (EditText)findViewById(R.id.input_phone);
        _update = (Button) findViewById(R.id.btn_update);
        setReady();
        getData();
    }
    private void setReady(){
        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoUpdate();
            }
        });
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
                    .load(Common.getInstance().getBaseURL()+"api/getOneClinicInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject doctor_object = result.getAsJsonObject("clinicInfo");
                                String id = doctor_object.get("id").getAsString();
                                String firstname = doctor_object.get("firstname").getAsString();
                                String secondname = doctor_object.get("secondname").getAsString();
                                String clinicname = doctor_object.get("clinicname").getAsString();
                                String mobile = doctor_object.get("mobile").getAsString();
                                _firstname.setText(firstname);
                                _secondname.setText(secondname);
                                _clinicname.setText(clinicname);
                                _phone.setText(mobile);
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void infoUpdate(){
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String clinicImage="";

        String firstname  = _firstname.getText().toString();
        String secondname  = _secondname.getText().toString();
        String clinicname  = _clinicname.getText().toString();
        String clinicphone  = _phone.getText().toString();


        JsonObject json = new JsonObject();
        json.addProperty("id", mClinicID);
        json.addProperty("firstname", firstname);
        json.addProperty("secondname", secondname);
        json.addProperty("clinicname", clinicname);
        json.addProperty("phone", clinicphone);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updateNormalClinicInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                Toast.makeText(getBaseContext(), "Update Success.", Toast.LENGTH_LONG).show();
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
        String firstname = _firstname.getText().toString();
        String secondname = _secondname.getText().toString();
        String clinicname = _clinicname.getText().toString();
        String phone = _phone.getText().toString();
        if (firstname.isEmpty()) {
            _firstname.setError("Input firstname");
            valid = false;
        } else {
            _firstname.setError(null);
        }
        if (secondname.isEmpty()) {
            _secondname.setError("Input secondname");
            valid = false;
        } else {
            _secondname.setError(null);
        }
        if (clinicname.isEmpty()) {
            _clinicname.setError("Input clinic name");
            valid = false;
        } else {
            _clinicname.setError(null);
        }
        if (phone.isEmpty()) {
            _phone.setError("Input phonenumber");
            valid = false;
        } else {
            _phone.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}