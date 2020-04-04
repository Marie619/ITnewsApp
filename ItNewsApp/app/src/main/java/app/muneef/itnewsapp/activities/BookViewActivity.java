package app.muneef.itnewsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;



import app.muneef.itnewsapp.R;
import app.muneef.itnewsapp.models.Books;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class BookViewActivity extends AppCompatActivity  implements DownloadFile.Listener {
   WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);


        Intent intent = getIntent();

        Books book = (Books) intent.getSerializableExtra("BOOK");

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        String pdf = book.getBookUrl();
        webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);



    }

    @Override
    public void onSuccess(String url, String destinationPath) {

    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}
