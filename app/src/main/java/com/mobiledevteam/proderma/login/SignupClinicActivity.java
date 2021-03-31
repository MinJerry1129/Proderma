package com.mobiledevteam.proderma.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Locale.getDefault;

public class SignupClinicActivity extends AppCompatActivity {
    private EditText _location;
    private EditText _info;
    private EditText _visa;
    private EditText _whatsapp;

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
    private LocationManager locationmanager;
    private Geocoder geocoder;
    private List<Address> addresses;
    private LatLng my_location;
    private Location LocationGps;

    List<Place.Field> fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
    );
//    private Object ImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_clinic);
        geocoder = new Geocoder(this, getDefault());
        if(!Places.isInitialized()){
//            Places.initialize(getBaseContext(),"AIzaSyDx0lfU-akX0HiFDtEUUIJ99rugOB95Ip4");
            Places.initialize(getBaseContext(),"AIzaSyB8RD2Pu5w7bv-UrhWA5dN1Brzdo-yf1SI");
        }
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(new Criteria(), true);
        my_location = new LatLng(48.85299,2.34288);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }else{
            LocationGps = locationmanager.getLastKnownLocation(provider);
        }

        mFirstname = getIntent().getStringExtra("firstname");
        mSecondname = getIntent().getStringExtra("secondname");
        mClinicname = getIntent().getStringExtra("clinicname");
        mEmail = getIntent().getStringExtra("email");
        mPhone = getIntent().getStringExtra("phone");
        mPassword = getIntent().getStringExtra("password");
        _location = (EditText)findViewById(R.id.input_cliniclocation);
        _info = (EditText)findViewById(R.id.input_clinicinfo);
        _visa = (EditText)findViewById(R.id.input_visacode);
        _whatsapp = (EditText)findViewById(R.id.input_whatsapp);
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
        _location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(getBaseContext());
                startActivityForResult(intent,101);
            }
        });
        if (LocationGps != null) {
            my_location = new LatLng(LocationGps.getLatitude(), LocationGps.getLongitude());
        }else{
            my_location = new LatLng(48.8499,2.3512);
        }
        Log.d("My location ::", String.valueOf(my_location));
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
        String whatsapp  = _whatsapp.getText().toString();
        String visacode  = _visa.getText().toString();
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
        json.addProperty("visacode",visacode);
        json.addProperty("whatsapp",whatsapp);
        json.addProperty("photo",clinicImage);
        json.addProperty("latitude",String.valueOf(my_location.latitude));
        json.addProperty("longitude",String.valueOf(my_location.longitude));

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
                                }else if (status.equals("noclinic")) {
                                    Toast.makeText(getBaseContext(),"You are not elite clinic, Please check visacode", Toast.LENGTH_LONG).show();
                                }else if (status.equals("existemail")) {
                                    Toast.makeText(getBaseContext(),"Your account already exist, Please contact to support team", Toast.LENGTH_LONG).show();
                                } else if (status.equals("fail")) {
                                    Toast.makeText(getBaseContext(),"Fail signup", Toast.LENGTH_LONG).show();
                                }else{
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
//        ImagePicker.create(this).returnMode(ReturnMode.ALL).folderMode(true).single().includeVideo(false).start();
//        ImagePicker.Companion.with(this).cropSquare().compress(300).maxResultSize(400,400).start(201);
        ImagePicker.Companion.with(this).saveDir(Environment.getExternalStorageDirectory()).cropSquare().maxResultSize(1000,1000).start();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                _location.setText(place.getAddress());
                my_location = place.getLatLng();
            }
        }else{
            if(resultCode == Activity.RESULT_OK){
                Uri fileUri = data.getData();
                mSelImageStatus = "yes";
                _txtSelImage.setVisibility(View.GONE);
                _imgClinic.setImageURI(fileUri);
                File file = ImagePicker.Companion.getFile(data);
                filePath = ImagePicker.Companion.getFilePath(data);
            }
        }
//        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
//            // or get a single image only
//            image = ImagePicker.getFirstImageOrNull(data);
//            if(image!=null) {
//                //imageView.setImageBitmap();
//                filePath=image.getPath();
//                mSelImageStatus = "yes";
//                _txtSelImage.setVisibility(View.GONE);
//                _imgClinic.setImageURI(Uri.parse(filePath));
//            }
//        }



        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;
        String location = _location.getText().toString();
        String visa = _visa.getText().toString();
        String whataspp = _whatsapp.getText().toString();
        if (whataspp.isEmpty()) {
            _whatsapp.setError("Input whatsappnumber");
            valid = false;
        } else {
            String expression = "^[+]+[0-9]{9,20}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(whataspp);
            if (matcher.matches()){
                _whatsapp.setError(null);
            }else{
                _whatsapp.setError("Input correct whatsapp number");
                valid = false;
            }

        }
        if (location.isEmpty()) {
            _location.setError("Input Address");
            valid = false;
        } else {
            _location.setError(null);
        }
        if (visa.isEmpty()) {
            _visa.setError("Input Elite code");
            valid = false;
        } else {
            String expression = "^[0-9]{16,17}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(visa);
            if (matcher.matches()){
                _visa.setError(null);
            }else{
                _visa.setError("Input correct Elite code");
                valid = false;
            }
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