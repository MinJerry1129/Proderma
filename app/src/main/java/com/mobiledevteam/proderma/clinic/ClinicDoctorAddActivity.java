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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ClinicDoctorAddActivity extends AppCompatActivity {
    private ImageView _imgDoctor;
    private TextView _selImg;
    private EditText _doctorname;
    private EditText _doctorage;
    private EditText _doctorinfo;
    private Button _btnadd;
    private String mselIamge = "no";
    private Image image;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_doctor_add);
        _imgDoctor = (ImageView)findViewById(R.id.img_doctor);
        _selImg = (TextView)findViewById(R.id.txt_selImage);
        _doctorname = (EditText)findViewById(R.id.input_doctorname);
        _doctorage = (EditText)findViewById(R.id.input_doctorage);
        _doctorinfo = (EditText)findViewById(R.id.input_doctorinfo);
        _btnadd = (Button)findViewById(R.id.btn_add);
        setReady();
    }
    private void setReady(){
        _imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });
        _btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDoctor();
            }
        });

    }
    private void addDoctor(){
        if (mselIamge != "yes"){
            Toast.makeText(getBaseContext(),"Please Select Doctor Image", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Doctor...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String doctorname  = _doctorname.getText().toString();
        String doctorage  = _doctorage.getText().toString();
        String doctorinfo  = _doctorinfo.getText().toString();
        String doctorImage = encodedImage;

        //String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();

        json.addProperty("id",Common.getInstance().getClinicID());
        json.addProperty("name",doctorname);
        json.addProperty("age",doctorage);
        json.addProperty("info",doctorinfo);
        json.addProperty("photo",doctorImage);


        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/addDoctor")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String status = result.get("status").getAsString();
                                if (status.equals("ok")) {
                                    Toast.makeText(getBaseContext(),"Add Success, please wait approve", Toast.LENGTH_LONG).show();
                                } else if (status.equals("fail")) {
                                    Toast.makeText(getBaseContext(),"Fail Add", Toast.LENGTH_LONG).show();
                                }
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void onPickImage() {
        ImagePicker.Companion.with(this).saveDir(Environment.getExternalStorageDirectory()).cropSquare().maxResultSize(700,700).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Uri fileUri = data.getData();
            mselIamge = "yes";
            _selImg.setVisibility(View.GONE);
            _imgDoctor.setImageURI(fileUri);
            File file = ImagePicker.Companion.getFile(data);
            filePath = ImagePicker.Companion.getFilePath(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _doctorname.getText().toString();
        String age = _doctorage.getText().toString();
        if (name.isEmpty()) {
            _doctorname.setError("Input Doctor name");
            valid = false;
        } else {
            _doctorname.setError(null);
        }
//        if (age.isEmpty()) {
//            _doctorage.setError("Input Doctor age");
//            valid = false;
//        } else {
//            _doctorage.setError(null);
//        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClinicDoctorAddActivity.this, ClinicHomeActivity.class);
        startActivity(intent);
        finish();
    }
}