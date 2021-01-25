package com.mobiledevteam.proderma.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mobiledevteam.proderma.R;

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
    private String[] mClinicInfo;

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

        Intent intent=new Intent(this, SignupClinicActivity.class)
                .putExtra("firstname", firstName)
                .putExtra("secondname", secondName)
                .putExtra("clinicname",clinicName)
                .putExtra("email", email)
                .putExtra("phone", phone)
                .putExtra("password", password);
        startActivity(intent);
        finish();
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