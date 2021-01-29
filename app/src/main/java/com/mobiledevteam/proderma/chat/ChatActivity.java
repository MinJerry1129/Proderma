package com.mobiledevteam.proderma.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobiledevteam.proderma.R;

public class ChatActivity extends AppCompatActivity {
    private EditText _msg;
    private Button _btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        _msg= (EditText)findViewById(R.id.input_msg);
        _btnSend = (Button)findViewById(R.id.btn_send);
        setReady();
    }
    private void setReady(){
        _btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMsg();
            }
        });
        boolean installed = appInstalledOrNot();
        if (!installed){
            Toast.makeText(getBaseContext(), "Whatsapp not installed on your device", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendMsg(){
        String message = _msg.getText().toString();
        boolean installed = appInstalledOrNot();
        if (installed){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+971553273607 &text="+message));
            startActivity(intent);
        }else {
            Toast.makeText(getBaseContext(), "Whatsapp not installed on your device", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean appInstalledOrNot(){
        PackageManager packageManager =getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}