package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.LanguageActivity;
import com.mobiledevteam.proderma.MainActivity;
import com.mobiledevteam.proderma.R;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static java.util.Locale.getDefault;

public class ClinicInfoEditActivity extends AppCompatActivity {
    private ImageView _imgClinic;
    private EditText _clinicName;
    private EditText _clinicLocation;
    private EditText _clinicPhone;
    private EditText _clinicWhatsapp;
    private EditText _clinicInfo;
    private Button _btnUpdate;
    private String mClinicID;
    private String mSelImg = "no";
    private Image image;
    private String filePath;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_info_edit);
        geocoder = new Geocoder(this, getDefault());
        mClinicID = Common.getInstance().getClinicID();
        _imgClinic = (ImageView)findViewById(R.id.img_clinic);
        _clinicName = (EditText)findViewById(R.id.input_clinicname);
        _clinicLocation = (EditText)findViewById(R.id.input_cliniclocation);
        _clinicPhone = (EditText)findViewById(R.id.input_clinicphone);
        _clinicWhatsapp = (EditText)findViewById(R.id.input_whatsapp);
        _clinicInfo = (EditText)findViewById(R.id.input_clinicinfo);
        _btnUpdate = (Button) findViewById(R.id.btn_update);
        setReady();
        getReady();
    }
    private void setReady(){
        _clinicLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(getBaseContext());
                startActivityForResult(intent,101);
            }
        });
        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClinicInfoEditActivity.this);
                builder1.setMessage("Do you want update Clinic Information?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                onClinicUpdate();
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
        _imgClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage();
            }
        });
    }
    private void onClinicUpdate(){
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
        if (mSelImg.equals("yes")){
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            clinicImage = encodedImage;
        }
        String clinicname  = _clinicName.getText().toString();
        String cliniclocation  = _clinicLocation.getText().toString();
        String clinicphone  = _clinicPhone.getText().toString();
        String clinicwhatsapp  = _clinicWhatsapp.getText().toString();
        String clinicinfo  = _clinicInfo.getText().toString();


        JsonObject json = new JsonObject();
        json.addProperty("id", mClinicID);
        json.addProperty("clinicname", clinicname);
        json.addProperty("location", cliniclocation);
        json.addProperty("latitude", String.valueOf(my_location.latitude));
        json.addProperty("longitude", String.valueOf(my_location.longitude));
        json.addProperty("phone", clinicphone);
        json.addProperty("whatsapp", clinicwhatsapp);
        json.addProperty("info", clinicinfo);
        json.addProperty("isChange", mSelImg);
        json.addProperty("photo",clinicImage);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updateClinicInfo")
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
    private void getReady(){
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
                                String clinicname = doctor_object.get("clinicname").getAsString();
                                String mobile = doctor_object.get("mobile").getAsString();
                                String whatsapp = doctor_object.get("whatsapp").getAsString();
                                String location = doctor_object.get("location").getAsString();
                                String info = doctor_object.get("information").getAsString();
                                String image = doctor_object.get("photo").getAsString();
                                String latitude = doctor_object.get("latitude").getAsString();
                                String longitude = doctor_object.get("longitude").getAsString();
                                my_location = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                                _clinicName.setText(clinicname);
                                _clinicPhone.setText(mobile);
                                _clinicWhatsapp.setText(whatsapp);
                                _clinicLocation.setText(location);
                                _clinicInfo.setText(info);
                                Ion.with(getBaseContext()).load(Common.getInstance().getBaseURL() + image).intoImageView(_imgClinic);
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
        if(requestCode == 101){
            if(resultCode == Activity.RESULT_OK && data != null){
                Place place = Autocomplete.getPlaceFromIntent(data);
                _clinicLocation.setText(place.getAddress());
                my_location = place.getLatLng();
            }
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            image = ImagePicker.getFirstImageOrNull(data);
            if(image!=null) {
                //imageView.setImageBitmap();
                filePath=image.getPath();
                mSelImg = "yes";
                _imgClinic.setImageURI(Uri.parse(filePath));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean validate() {
        boolean valid = true;
        String name = _clinicName.getText().toString();
        String location = _clinicLocation.getText().toString();
        String phone = _clinicPhone.getText().toString();
        String whatsapp = _clinicWhatsapp.getText().toString();
        if (name.isEmpty()) {
            _clinicName.setError("Input clinic name");
            valid = false;
        } else {
            _clinicName.setError(null);
        }
        if (location.isEmpty()) {
            _clinicLocation.setError("Input clinic location");
            valid = false;
        } else {
            _clinicLocation.setError(null);
        }
        if (phone.isEmpty()) {
            _clinicPhone.setError("Input phonenumber");
            valid = false;
        } else {
            _clinicPhone.setError(null);
        }
        if (whatsapp.isEmpty()) {
            _clinicWhatsapp.setError("Input phonenumber");
            valid = false;
        } else {
            _clinicWhatsapp.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ClinicInfoEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
        startActivity(intent);
        finish();
    }
}