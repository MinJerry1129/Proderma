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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;

public class ClinicOfferEditActivity extends AppCompatActivity {
    private String mOfferID;
    private EditText _offerTitle;
    private EditText _offerInfo;
    private Button _btnRemove;
    private TextView _txtStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_offer_edit);
        mOfferID = getIntent().getStringExtra("offer_id");
        _offerTitle = (EditText) findViewById(R.id.input_offertitle);
        _offerInfo = (EditText) findViewById(R.id.input_offerinfo);
        _btnRemove = (Button) findViewById(R.id.btn_delete);
        _txtStatus = (TextView) findViewById(R.id.txt_approveStatus);
        setReady();
        getData();
    }
    private void setReady(){
        _btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ClinicOfferEditActivity.this);
                builder1.setMessage("Do you want delete this offer?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                offerRemove();
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
    private void offerRemove(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mOfferID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/deleteOffer")
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
                                    Intent intent=new Intent(ClinicOfferEditActivity.this, ClinicHomeActivity.class);//LoginActivity.class);
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
        json.addProperty("id", mOfferID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getOfferInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonObject doctor_object = result.getAsJsonObject("offerInfo");
                                String id = doctor_object.get("id").getAsString();
                                String title = doctor_object.get("title").getAsString();
                                String info = doctor_object.get("description").getAsString();
                                String status = doctor_object.get("status").getAsString();
                                if(status.equals("enable")){
                                    _txtStatus.setText("Approved");
                                }else{
                                    _txtStatus.setText("waiting approve");
                                }
                                _offerTitle.setText(title);
                                _offerInfo.setText(info);
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}