package com.mobiledevteam.proderma.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeProduct;
import com.mobiledevteam.proderma.home.HomeActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {
    private EditText _email;
    private EditText _password;
    private Button _btnLogin;
    private TextView _txtSignup;
    private String phone_token;
    private String loginStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        _email = (EditText)findViewById(R.id.input_email);
        _password = (EditText)findViewById(R.id.input_password);
        _btnLogin = (Button) findViewById(R.id.btn_signin);
        _txtSignup = (TextView) findViewById(R.id.txt_signup);
        phone_token = FirebaseInstanceId.getInstance().getToken();

        setReady();
    }
    private void setReady(){
        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        _txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void Login(){
        if(!validate()){
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("email", _email.getText().toString());
        json.addProperty("password", _password.getText().toString());
        json.addProperty("phonetoken", phone_token);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/login")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                String id = result.get("clinic_id").toString();
                                Log.d("clinic_info::", id);
                                if(id.equals("\"nouser\"")){
                                    Toast.makeText(getBaseContext(),"You are not registered, Please signup",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"wrongpassword\"")){
                                    Toast.makeText(getBaseContext(),"Please input correct password",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"deleted\"")){
                                    Toast.makeText(getBaseContext(),"Your account is deleted, Please contact to support team",Toast.LENGTH_LONG).show();
                                }else if (id.equals("\"waiting\"")){
                                    Toast.makeText(getBaseContext(),"Your account is not accepted, Please conatct to support team",Toast.LENGTH_LONG).show();
                                }else{
                                    JsonObject clinic_object = result.getAsJsonObject("clinic_id");
                                    String clinic_id = clinic_object.get("id").getAsString();
                                    String type = clinic_object.get("type").getAsString();
                                    Common.getInstance().setClinicID(clinic_id);
                                    Common.getInstance().setLogin_status("yes");
                                    Common.getInstance().setClinictype(type);
                                    loginStatus = "yes" + " " + clinic_id + " " + type;
                                    writeFile();
                                }
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private  void writeFile(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("loginstatus.pdm", MODE_PRIVATE);
            fileOutputStream.write(loginStatus.getBytes());
            fileOutputStream.close();

            Intent intent=new Intent(getBaseContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }catch (FileNotFoundException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, LoginHomeActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean validate() {
        boolean valid = true;
        String email = _email.getText().toString();
        String password = _password.getText().toString();
        if (email.isEmpty()) {
            _email.setError("Input Email");
            valid = false;
        } else {
            _email.setError(null);
        }
        if (password.isEmpty()) {
            _password.setError("Input Password");
            valid = false;
        } else {
            _password.setError(null);
        }
        return valid;
    }
}