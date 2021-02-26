package com.mobiledevteam.proderma.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.LocaleHelper;
import com.mobiledevteam.proderma.MainActivity;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.clinic.ClinicHomeActivity;
import com.mobiledevteam.proderma.home.HomeActivity;
import com.mobiledevteam.proderma.login.LoginHomeActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    private Button _btnLogout;
    private Button _selLang;
    private Button _setPassword;
    private String sel_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        _selLang = (Button)findViewById(R.id.btn_sellang);
        _setPassword = (Button)findViewById(R.id.btn_setpass);
        _btnLogout = (Button)findViewById(R.id.btn_logout);
        setReady();
    }
    private void setReady(){
        if(Common.getInstance().getLogin_status().equals("no")){
            _btnLogout.setVisibility(View.GONE);
            _setPassword.setVisibility(View.GONE);
        }
        _btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.getInstance().setLogin_status("no");
                Intent intent=new Intent(getBaseContext(), LoginHomeActivity.class);//LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        _setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getBaseContext(), ChangePasswordActivity.class);//LoginActivity.class);
                startActivity(intent);
            }
        });
        String selLang = Common.getInstance().getSelLang();
        if (selLang.equals("en")){
            _selLang.setText("عربي");
        }else{
            _selLang.setText("English");
        }

        _selLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selLang.equals("en")){
                    Common.getInstance().setSelLang("ar");
                    sel_lang = "ar";
                    writeFile();
                    final Configuration configuration = getResources().getConfiguration();
                    LocaleHelper.setLocale(getBaseContext(), "ar");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        configuration.setLayoutDirection(new Locale("ar"));
                    }
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                    Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    sel_lang = "en";
                    writeFile();
                    Common.getInstance().setSelLang("en");
                    final Configuration configuration = getResources().getConfiguration();
                    LocaleHelper.setLocale(getBaseContext(), "en");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        configuration.setLayoutDirection(new Locale("en"));
                    }
                    getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
                    Intent intent=new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private  void writeFile(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("lang.pdm", MODE_PRIVATE);
            fileOutputStream.write(sel_lang.getBytes());
            fileOutputStream.close();
        }catch (FileNotFoundException e){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}