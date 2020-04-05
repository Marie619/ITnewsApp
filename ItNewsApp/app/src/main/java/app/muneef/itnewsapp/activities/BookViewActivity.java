package app.muneef.itnewsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.Books;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class BookViewActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar pbLoading;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);


        //  Intent intent = getIntent();


        //  Books book = (Books) intent.getSerializableExtra("BOOK");

        webview = findViewById(R.id.webView);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);
        String pdf = getIntent().getStringExtra("book_url");
        try {
            url = URLEncoder.encode(pdf, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                getSupportActionBar().setTitle("Loading");
                if (newProgress == 100) {
                    pbLoading.setVisibility(View.GONE);
                    getSupportActionBar().setTitle(R.string.app_name);
                }
            }
        });


    }


}
