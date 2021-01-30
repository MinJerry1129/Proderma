package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.mobiledevteam.proderma.Common;
import com.mobiledevteam.proderma.R;
import com.mobiledevteam.proderma.cell.AllPdf;
import com.mobiledevteam.proderma.cell.AllPdfAdapter;
import com.mobiledevteam.proderma.cell.AllProductAdapter;
import com.mobiledevteam.proderma.cell.AllVideo;
import com.mobiledevteam.proderma.cell.AllVideoAdapter;
import com.mobiledevteam.proderma.cell.HomeClinic;
import com.mobiledevteam.proderma.cell.HomeProduct;

import java.io.File;
import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    private Button _video;
    private Button _pdf;
    private GridView _allGridView;
    ArrayList<AllVideo> mAllVideoList = new ArrayList<>();
    ArrayList<AllPdf> mAllPdfList = new ArrayList<>();
    private String sel_type="video";
    private String mProductID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        mProductID = getIntent().getStringExtra("product_id");
        _allGridView = (GridView)findViewById(R.id.grid_alltrain);
        _video = (Button) findViewById(R.id.btn_video);
        _pdf = (Button) findViewById(R.id.btn_pdf);
        setReady();
        getData();
    }
    private void setReady(){
        _video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pdf.setBackgroundResource(R.drawable.button_background_light_major);
                _video.setBackgroundResource(R.drawable.button_background_major);
                sel_type= "video";
                initView();
            }
        });
        _pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _video.setBackgroundResource(R.drawable.button_background_light_major);
                _pdf.setBackgroundResource(R.drawable.button_background_major);
                sel_type= "pdf";
                initView();
            }
        });
        _allGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sel_type.equals("video")){
                    watchYoutubeVideo(mAllVideoList.get(position).getmUrl());
                }else{
//                    Intent intent = new Intent(getApplicationContext(), PdfViewerActivity.class).putExtra("url", mAllPdfList.get(position).getmUrl());
//                    startActivity(intent);
//                    finish();
                    openPdf(mAllPdfList.get(position).getmUrl());
                }

            }
        });

    }
    private void getData(){
        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Bright_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        JsonObject json = new JsonObject();
        json.addProperty("id", mProductID);

        try {
            Ion.with(this)
                    .load(Common.getInstance().getBaseURL()+"api/getTrainInfo")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.dismiss();
//                            Log.d("result::", result.toString());
                            if (result != null) {
                                JsonArray videos_array = result.get("videoInfo").getAsJsonArray();
                                for(JsonElement videoElement : videos_array){
                                    JsonObject thevideo = videoElement.getAsJsonObject();
                                    String id = thevideo.get("id").getAsString();
                                    String title = thevideo.get("title").getAsString();
                                    String description = thevideo.get("information").getAsString();
                                    String url = thevideo.get("url").getAsString();
                                    mAllVideoList.add(new AllVideo(id,title,description,url));
                                }
                                JsonArray pdfs_array = result.get("pdfInfo").getAsJsonArray();
                                for(JsonElement pdfElement : pdfs_array){
                                    JsonObject thepdf = pdfElement.getAsJsonObject();
                                    String id = thepdf.get("id").getAsString();
                                    String title = thepdf.get("title").getAsString();
                                    String description = thepdf.get("information").getAsString();
                                    String url = thepdf.get("url").getAsString();
                                    mAllPdfList.add(new AllPdf(id,title,description,url));
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
        if(sel_type.equals("video")){
            AllVideoAdapter adapter_video = new AllVideoAdapter(getBaseContext(), mAllVideoList);
            _allGridView.setAdapter(adapter_video);
        }else{
            AllPdfAdapter adapter_pdf = new AllPdfAdapter(getBaseContext(), mAllPdfList);
            _allGridView.setAdapter(adapter_pdf);
        }

    }
    public void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
    public void openPdf(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), OneProductActivity.class).putExtra("product_id", mProductID);
        startActivity(intent);
        finish();
    }
}