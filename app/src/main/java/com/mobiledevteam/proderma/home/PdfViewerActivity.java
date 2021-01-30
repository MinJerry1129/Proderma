package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobiledevteam.proderma.R;

public class PdfViewerActivity extends AppCompatActivity {
    WebView _webviwer;
    private String pdf_url;
    Activity activity ;
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdf_url = getIntent().getStringExtra("url");
        _webviwer = (WebView) findViewById(R.id.webviwer);
        setReady();
    }
    private void setReady(){
        _webviwer.getSettings().setJavaScriptEnabled(true);
        _webviwer.getSettings().setLoadWithOverviewMode(true);
        _webviwer.getSettings().setUseWideViewPort(true);
        _webviwer.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
            }
        });

        _webviwer.loadUrl("http://www.africau.edu/images/default/sample.pdf");
    }
}