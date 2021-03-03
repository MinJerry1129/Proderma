package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.ClinicDoctor;
import com.mobiledevteam.proderma.cell.HomeClinic;

import java.io.ByteArrayOutputStream;

public class ClinicDoctorEditActivity extends AppCompatActivity {
    private ImageView _imgDoctor;
    private EditText _docName;
    private EditText _docAge;
    private EditText _docInfo;
    private Button _btnUpdate;
    private Button _btnRemove;
    private TextView _txtStatus;
    private String mDoctorID;
    private String mSelImg = "no";
    private Image image;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_doctor_edit);
        mDoctorID = getIntent().getStringExtra("doctor_id");
        _imgDoctor = (ImageView)findViewById(R.id.img_doctor);
        _txtStatus = (TextView) findViewById(R.id.txt_approveStatus);
        _docName = (EditText) findViewById(R.id.input_doctorname);
        _docAge = (EditText) findViewById(R.id.input_doctorage);
        _docInfo = (EditText) findViewById(R.id.input_doctorinfo);
        _btnUpdate = (Button) findViewById(R.id.btn_add);
        _btnRemove = (Button) findViewById(R.id.btn_delete);
        setReady();
        getData();
    }
    private void setReady(){
        _imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });
        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClinicDoctorEditActivity.this);
                builder1.setMessage("Do you want update this doctor?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                doctorUpdate();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                ).show ();
            }
        });
        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClinicDoctorEditActivity.this);
                builder1.setMessage("Do you want delete this doctor?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                doctorRemove();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                ).show ();
            }
        });

    }
    private void doctorUpdate()
    {
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String doctorImage="";
        if (mSelImg.equals("yes")){
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            doctorImage = encodedImage;
        }
        String doctorname  = _docName.getText().toString();
        String doctorage  = _docAge.getText().toString();
        String doctorinfo  = _docInfo.getText().toString();


        JsonObject json = new JsonObject();
        json.addProperty("id", mDoctorID);
        json.addProperty("name", doctorname);
        json.addProperty("age", doctorage);
        json.addProperty("info", doctorinfo);
        json.addProperty("isChange", mSelImg);
        json.addProperty("photo",doctorImage);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updateDoctorInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                Toast.makeText(getBaseContext(), "Update Success.", Toast.LENGTH_LONG).show();
                                _txtStatus.setText("waiting approve");
//                                Intent intent=new Intent(ClinicDoctorEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
//                                startActivity(intent);
//                                finish();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void doctorRemove(){
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
                    .load(Common.getInstance().getBaseURL()+"api/deleteDoctor")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String result1 = result.get("result").getAsString();
                                if (result1.equals("success")){
                                    Intent intent=new Intent(ClinicDoctorEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{

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
                                JsonObject doctor_object = result.getAsJsonObject("doctorInfo");
                                String id = doctor_object.get("id").getAsString();
                                String name = doctor_object.get("name").getAsString();
                                String age = doctor_object.get("age").getAsString();
                                String info = doctor_object.get("information").getAsString();
                                String image = doctor_object.get("photo").getAsString();
                                String status = doctor_object.get("status").getAsString();
                                if(status.equals("enable")){
                                    _txtStatus.setText("Approved");
                                }else{
                                    _txtStatus.setText("waiting approve");
                                }
                                _docName.setText(name);
                                _docAge.setText(age);
                                _docInfo.setText(info);
                                Ion.with(getBaseContext()).load(Common.getInstance().getBaseURL() + image).intoImageView(_imgDoctor);
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    public void onPickImage() {
        ImagePicker.create(this).single().includeVideo(false).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            image = ImagePicker.getFirstImageOrNull(data);
            if(image!=null) {
                //imageView.setImageBitmap();
                filePath=image.getPath();
                mSelImg = "yes";
                _imgDoctor.setImageURI(Uri.parse(filePath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;
        String name = _docName.getText().toString();
        String age = _docAge.getText().toString();
        if (name.isEmpty()) {
            _docName.setError("Input Doctor name");
            valid = false;
        } else {
            _docName.setError(null);
        }
//        if (age.isEmpty()) {
//            _docAge.setError("Input Doctor age");
//            valid = false;
//        } else {
//            _docAge.setError(null);
//        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ClinicDoctorEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }
}