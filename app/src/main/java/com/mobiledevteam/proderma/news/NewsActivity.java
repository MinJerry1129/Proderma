package com.mobiledevteam.proderma.news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllClinicAdapter;
import com.mobiledevteam.proderma.cell.AllNewsAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.News;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<News> mAllNews = new ArrayList<>();
    private String loginStatus;
    private String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        _allGridView = (GridView)findViewById(R.id.grid_allNews);
        loginStatus = Common.getInstance().getLogin_status();
        userType = Common.getInstance().getClinictype();
        getData();
    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("email", "");
        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getNotification")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            if (result != null) {
                                JsonArray notifications_array = result.get("notificationsInfo").getAsJsonArray();
                                for(JsonElement notificationElement : notifications_array){
                                    JsonObject thenews = notificationElement.getAsJsonObject();
                                    String id = thenews.get("id").getAsString();
                                    String title = thenews.get("title").getAsString();
                                    String datetime = thenews.get("date").getAsString();
                                    String description = thenews.get("description").getAsString();
                                    String type = thenews.get("type").getAsString();
                                    if(type.equals("all")){
                                        mAllNews.add(new News(id,title,datetime,description,type));
                                    }
                                    if(loginStatus.equals("yes")){
                                        if(type.equals(userType)){
                                            mAllNews.add(new News(id,title,datetime,description,type));
                                        }
                                    }
                                }
                                initView();
                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void initView(){
        AllNewsAdapter adapter_clinic = new AllNewsAdapter(getBaseContext(), mAllNews);
        _allGridView.setAdapter(adapter_clinic);
    }
}