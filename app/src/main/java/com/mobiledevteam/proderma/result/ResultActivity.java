package com.mobiledevteam.proderma.result;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllProductAdapter;
import com.mobiledevteam.proderma.cell.Brand;
import com.mobiledevteam.proderma.cell.BrandAdapter;
import com.mobiledevteam.proderma.cell.HomeProduct;
import com.mobiledevteam.proderma.cell.Result;
import com.mobiledevteam.proderma.cell.ResultAdapter;
import com.mobiledevteam.proderma.home.HomeActivity;
import com.mobiledevteam.proderma.home.OneProductActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    private GridView _allGridView;
    ArrayList<Result> mAllResult = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        _allGridView = (GridView)findViewById(R.id.grid_result);
        setReady();
        getReady();
    }

    private void setReady() {
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Common.getInstance().setImgUrl(Common.getInstance().getBaseURL() + mAllResult.get(position).getmImageurl());
                Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getReady() {
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
                    .load(Common.getInstance().getBaseURL()+"api/getGallery")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonArray gallerys_array = result.get("galleryInfo").getAsJsonArray();
                                if (gallerys_array.size() > 0){
                                    for(JsonElement galleryElement : gallerys_array){
                                        JsonObject thegallery = galleryElement.getAsJsonObject();
                                        String id = thegallery.get("id").getAsString();
                                        String imageurl = thegallery.get("imageurl").getAsString();
                                        mAllResult.add(new Result(id,imageurl));
                                    }
                                    initView();
                                }else{
                                    Toast.makeText(getBaseContext(), "No Result", Toast.LENGTH_LONG).show();
                                }


                            } else {

                            }
                        }
                    });
        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void initView(){
        this.runOnUiThread(new Runnable() {
            public void run() {
                ResultAdapter adapter_result = new ResultAdapter(getBaseContext(), mAllResult);
                _allGridView.setAdapter(adapter_result);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getBaseContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


}