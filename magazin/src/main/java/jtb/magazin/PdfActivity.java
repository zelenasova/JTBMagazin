package jtb.magazin;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;


public class PdfActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float screenWidth = metrics.widthPixels;
        float dx = screenWidth/Constants.WIDTH_KOEF;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.getLayoutParams().width=(int) (30*dx);
        imageView.getLayoutParams().height=(int) (30*dx);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        WebView webView = (WebView) findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=http://194.50.215.138/privacy.pdf");

    }



}
