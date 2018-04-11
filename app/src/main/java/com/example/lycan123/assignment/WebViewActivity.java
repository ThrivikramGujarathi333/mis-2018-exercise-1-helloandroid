package com.example.lycan123.assignment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity
{
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this); // *View inherits View Class
        setContentView(webView);
        startWebView(getIntent().getStringExtra("url"));
    }
    private void startWebView(String url) {

        // Javascript enabled on WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);

        //Load url in WebView
        webView.loadUrl(url);

        webView.setWebViewClient(new MyWebViewClient());
    }

    // This is invoked whenever a Back Button is pressed
    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class MyWebViewClient extends WebViewClient {

        ProgressDialog progressDialog;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            try {
                view.loadUrl("javascript:document.getElementById(\"appheader\").setAttribute(\"style\",\"display:none;\");");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //Show loader on url load
        @Override
        public void onLoadResource(WebView view, String url) {

            if (progressDialog == null) {
                // in standard case YourActivity.this
                progressDialog = new ProgressDialog(WebViewActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            try {

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                view.loadUrl("javascript:document.getElementById(\"appheader\").setAttribute(\"style\",\"display:none;\");");
            } catch (Exception e) {
                     e.printStackTrace();
            }
        }
    }
}
