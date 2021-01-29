package com.mobiledevteam.proderma.home;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

//import com.github.barteksc.pdfviewer.PDFView;
import com.mobiledevteam.proderma.R;

public class PdfViewerActivity extends AppCompatActivity {
//    private PDFView _pdfviwer;
    private String pdf_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdf_url = getIntent().getStringExtra("url");
//        _pdfviwer = (PDFView)findViewById(R.id.pdf_viewer);
        setReady();
    }
    private void setReady(){
//        _pdfviwer.fromUri(Uri.parse(pdf_url)).load();
//        _pdfviwer.fromUri(Uri.parse("http://www.africau.edu/images/default/sample.pdf")).load();

    }
}