package com.mobiledevteam.proderma.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.clinic.ClinicHomeActivity;
import com.mobiledevteam.proderma.home.HomeActivity;

public class LoginHomeActivity extends AppCompatActivity {
    private Button _btnLogin;
    private Button _btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_home);
        _btnLogin = (Button)findViewById(R.id.btn_signin);
        _btnSignup = (Button)findViewById(R.id.btn_signup);
        setReady();
    }
    private void setReady(){
        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}