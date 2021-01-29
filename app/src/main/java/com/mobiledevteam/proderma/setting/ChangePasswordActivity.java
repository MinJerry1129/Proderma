package com.mobiledevteam.proderma.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText _oPassword;
    private EditText _nPassword;
    private EditText _cPassword;
    private Button _update;
    private String mClinicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mClinicID = Common.getInstance().getClinicID();
        _oPassword = (EditText)findViewById(R.id.input_originalpass);
        _nPassword = (EditText)findViewById(R.id.input_newpass);
        _cPassword = (EditText)findViewById(R.id.input_confirmpass);
        _update = (Button) findViewById(R.id.btn_update);
        setReady();
    }
    private void setReady(){
        _update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
    }
    private void updatePassword(){
        if (!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();

        String opassword = _oPassword.getText().toString();
        String npassword = _nPassword.getText().toString();

        JsonObject json = new JsonObject();
        json.addProperty("id", mClinicID);
        json.addProperty("original", opassword);
        json.addProperty("new", npassword);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/updatePassword")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result123::", result.toString());
                            if (result != null) {
                                String result_update = result.get("status").getAsString();
                                if (result_update.equals("wrongpassword")){
                                    _oPassword.setError("Check Original password");
                                }else if (result_update.equals("ok")){
                                    Toast.makeText(getBaseContext(), "Update Success.", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getBaseContext(), "Update Fail.", Toast.LENGTH_LONG).show();
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
        String opassword = _oPassword.getText().toString();
        String npassword = _nPassword.getText().toString();
        String cpassword = _cPassword.getText().toString();
        if (opassword.isEmpty()) {
            _oPassword.setError("Input Original password");
            valid = false;
        } else {
            _oPassword.setError(null);
        }
        if (npassword.isEmpty()) {
            _nPassword.setError("Input new pasword");
            valid = false;
        } else {
            if(npassword.equals(cpassword)){
                _nPassword.setError(null);
                _cPassword.setError(null);
            }else{
                _nPassword.setError("Pasword not matched");
                _cPassword.setError("Pasword not matched");
                valid = false;
            }
        }

        return valid;
    }
}