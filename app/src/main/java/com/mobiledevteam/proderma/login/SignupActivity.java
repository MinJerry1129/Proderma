package com.mobiledevteam.proderma.login;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private EditText _firstName;
    private EditText _secondName;
    private EditText _clinicName;
    private EditText _email;
    private EditText _phone;
    private EditText _password;
    private TextView _singin;
    private Button _confirm;
    private CheckBox _chbClinic;
    private String clinic_type ="normal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        _firstName = (EditText)findViewById(R.id.input_firstname);
        _secondName = (EditText)findViewById(R.id.input_secondname);
        _clinicName = (EditText)findViewById(R.id.input_clinicname);
        _email = (EditText)findViewById(R.id.input_email);
        _phone = (EditText)findViewById(R.id.input_phone);
        _password = (EditText)findViewById(R.id.input_password);
        _singin = (TextView) findViewById(R.id.txt_signin);
        _confirm = (Button) findViewById(R.id.btn_confirm);
        _chbClinic = (CheckBox)findViewById(R.id.check_clinic);
        setReady();
    }
    private void setReady(){
        _singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm();
            }
        });
        _chbClinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                // Check which checkbox was clicked
                if (checked){
                    clinic_type = "elite";
                    _confirm.setText(getResources().getText(R.string.signup_enter));
                }
                else{
                    clinic_type = "normal";
                    _confirm.setText(getResources().getText(R.string.login_signup));
                    // Do your coding
                }
            }
        });
    }
    private void confirm(){
        if(!validate()){
            return;
        }
        String firstName = _firstName.getText().toString();
        String secondName = _secondName.getText().toString();
        String clinicName = _clinicName.getText().toString();
        String email = _email.getText().toString();
        String phone = _phone.getText().toString();
        String password = _password.getText().toString();
        if(clinic_type .equals("elite")){
            Intent intent=new Intent(this, SignupClinicActivity.class)
                    .putExtra("firstname", firstName)
                    .putExtra("secondname", secondName)
                    .putExtra("clinicname",clinicName)
                    .putExtra("email", email)
                    .putExtra("phone", phone)
                    .putExtra("password", password);
            startActivity(intent);
            finish();
        }else{
            final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating Account...");
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.show();

            // TODO: Implement your own signup logic here.
            JsonObject json = new JsonObject();
            json.addProperty("firstname",firstName);
            json.addProperty("secondname",secondName);
            json.addProperty("clinicname",clinicName);
            json.addProperty("email", email);
            json.addProperty("phone",phone);
            json.addProperty("password",password);

            try {
                Ion.with(this)
                        .load(Common.getInstance().getBaseURL()+"api/signup_normal")
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
                                        Toast.makeText(getBaseContext(),"Signup Success, Please wait accept or contact to support team", Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(SignupActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
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


    }

    public boolean validate() {
        boolean valid = true;
        String firstName = _firstName.getText().toString();
        String secondName = _secondName.getText().toString();
        String clinicName = _clinicName.getText().toString();
        String email = _email.getText().toString();
        String phone = _phone.getText().toString();
        String password = _password.getText().toString();
        if (firstName.isEmpty()) {
            _firstName.setError("Input Firstname");
            valid = false;
        } else {
            _firstName.setError(null);
        }
        if (secondName.isEmpty()) {
            _secondName.setError("Input Secondname");
            valid = false;
        } else {
            _secondName.setError(null);
        }
        if (clinicName.isEmpty()) {
            _clinicName.setError("Input Clinic name");
            valid = false;
        } else {
            _clinicName.setError(null);
        }
        if (email.isEmpty()) {
            _email.setError("Input Email");
            valid = false;
        } else {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()){
                _email.setError(null);
            }else{
                _email.setError("Input Email");
                valid = false;
            }
        }
        if (phone.isEmpty()) {
            _phone.setError("Input Phone");
            valid = false;
        } else {
            _phone.setError(null);
        }
        if (password.isEmpty()) {
            _password.setError("Input Password");
            valid = false;
        } else {
            _password.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, LoginHomeActivity.class);
        startActivity(intent);
        finish();
    }
}