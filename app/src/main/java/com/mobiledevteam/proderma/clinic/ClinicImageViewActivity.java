package com.mobiledevteam.proderma.clinic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

public class ClinicImageViewActivity extends AppCompatActivity {
    private ImageView _clinicImage;
    private Button _btnDel;
    private String mImageId;
    private String mImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_image_view);
        mImageId = getIntent().getStringExtra("imageid");
        mImageUrl = getIntent().getStringExtra("imageurl");
        _clinicImage = (ImageView)findViewById(R.id.img_clinicImage);
        _btnDel = (Button)findViewById(R.id.btn_delete);
        setReady();
    }
    private void setReady(){
        Ion.with(this).load(mImageUrl).intoImageView(_clinicImage);
        _btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClinicImageViewActivity.this);
                builder1.setMessage("Do you want delete this image?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                deleteImage();
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
    private void deleteImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mImageId);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/deleteClinicImage")
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
                                    Intent intent = new Intent(ClinicImageViewActivity.this, ClinicImageActivity.class);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClinicImageViewActivity.this, ClinicImageActivity.class);
        startActivity(intent);
        finish();
    }
}