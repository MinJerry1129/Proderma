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

import java.io.ByteArrayOutputStream;

public class ClinicOfferAddActivity extends AppCompatActivity {
    private EditText _offertitle;
    private EditText _offerinfo;
    private Button _btnadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_offer_add);
        _offertitle = (EditText)findViewById(R.id.input_offertitle);
        _offerinfo = (EditText)findViewById(R.id.input_offerinfo);
        _btnadd = (Button)findViewById(R.id.btn_add);
        setReady();
    }
    private void setReady(){
        _btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOffer();
            }
        });

    }
    private void addOffer(){
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding Doctor...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        String offerTitle  = _offertitle.getText().toString();
        String offerInfo  = _offerinfo.getText().toString();


        // TODO: Implement your own signup logic here.
        JsonObject json = new JsonObject();

        json.addProperty("id", Common.getInstance().getClinicID());
        json.addProperty("title",offerTitle);
        json.addProperty("info",offerInfo);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/addOffer")
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
    public boolean validate() {
        boolean valid = true;
        String title = _offertitle.getText().toString();
        String info = _offerinfo.getText().toString();
        if (title.isEmpty()) {
            _offertitle.setError("Input Offer title");
            valid = false;
        } else {
            _offertitle.setError(null);
        }
        if (info.isEmpty()) {
            _offerinfo.setError("Input offer info");
            valid = false;
        } else {
            _offerinfo.setError(null);
        }
        return valid;
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClinicOfferAddActivity.this, ClinicHomeActivity.class);
        startActivity(intent);
        finish();
    }
}