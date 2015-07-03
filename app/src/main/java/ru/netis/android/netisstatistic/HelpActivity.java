package ru.netis.android.netisstatistic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        WebView browser = (WebView)findViewById(R.id.helpWebView);

        WebSettings settings = browser.getSettings();
        settings.setJavaScriptEnabled(true);

        browser.loadUrl("file:///android_asset/help.html");
    }
}
