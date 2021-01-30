package com.mobiledevteam.proderma.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupClinicActivity extends AppCompatActivity {
    private EditText _location;
    private EditText _info;

    private ImageView _imgClinic;
    private TextView _txtSelImage;

    private Button _singup;
    private String mFirstname;
    private String mSecondname;
    private String mClinicname;
    private String mEmail;
    private String mPhone;
    private String mPassword;
    private String mSelImageStatus = "no";
    private Image image;
    private String filePath;

    private String signup_status = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_clinic);
        mFirstname = getIntent().getStringExtra("firstname");
        mSecondname = getIntent().getStringExtra("secondname");
        mClinicname = getIntent().getStringExtra("clinicname");
        mEmail = getIntent().getStringExtra("email");
        mPhone = getIntent().getStringExtra("phone");
        mPassword = getIntent().getStringExtra("password");
        _location = (EditText)findViewById(R.id.input_cliniclocation);
        _info = (EditText)findViewById(R.id.input_clinicinfo);
        _imgClinic = (ImageView) findViewById(R.id.img_clinic);
        _txtSelImage = (TextView) findViewById(R.id.txt_selImage);
        _singup = (Button) findViewById(R.id.btn_signup);
        setReady();
    }
    private void setReady(){
        _singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
        _imgClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });
    }
    private void Signup(){
        if (mSelImageStatus != "yes"){
            Toast.makeText(getBaseContext(),"Please Select Clinic Image", Toast.LENGTH_LONG).show();
            return;
        }
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bm = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String cliniclocation  = _location.getText().toString();
        String clinicinfo  = _info.getText().toString();
        String clinicImage = encodedImage;

        //String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();
        json.addProperty("firstname",mFirstname);
        json.addProperty("secondname",mSecondname);
        json.addProperty("clinicname",mClinicname);
        json.addProperty("email", mEmail);
        json.addProperty("phone",mPhone);
        json.addProperty("password",mPassword);
        json.addProperty("location",cliniclocation);
        json.addProperty("info",clinicinfo);
        json.addProperty("photo",clinicImage);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/signup")
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
                                    signup_status = "yes";
                                    Toast.makeText(getBaseContext(),"Signup Success, Please wait accept or contact to support team", Toast.LENGTH_LONG).show();
                                }else if (status.equals("existemail")) {
                                    Toast.makeText(getBaseContext(),"Your account already exist, Please contact to support team", Toast.LENGTH_LONG).show();
                                } else if (status.equals("fail")) {
                                    Toast.makeText(getBaseContext(),"Fail signup", Toast.LENGTH_LONG).show();
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
                mSelImageStatus = "yes";
                _txtSelImage.setVisibility(View.GONE);
                _imgClinic.setImageURI(Uri.parse(filePath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;
        String location = _location.getText().toString();
        if (location.isEmpty()) {
            _location.setError("Input Address");
            valid = false;
        } else {
            _location.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        if(signup_status.equals("no")){
            Intent intent=new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this, SigninActivity.class);
            startActivity(intent);
            finish();
        }

    }
}