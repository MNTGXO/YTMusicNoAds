package com.example.ytmusicnoads;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        setupWebView();
        webView.loadUrl("https://music.youtube.com");
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                injectAdBlockJS();
                injectBackgroundPlayJS();
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
    }

    private void injectAdBlockJS() {
        String adBlockJS = "javascript:(function() {" +
            "function blockAds() {" +
            "   const selectors = [" +
            "       '.video-ads', '.ad-showing'," +
            "       '.ad-container', '.ad-interrupting'," +
            "       '.ad-module', '.ytp-ad-module'" +
            "   ];" +
            "   selectors.forEach(selector => {" +
            "       document.querySelectorAll(selector).forEach(el => el.remove());" +
            "   });" +
            "   const iframes = document.getElementsByTagName('iframe');" +
            "   for (let i = 0; i < iframes.length; i++) {" +
            "       if (iframes[i].src.includes('ads')) {" +
            "           iframes[i].remove();" +
            "       }" +
            "   }" +
            "}" +
            "setInterval(blockAds, 1000);" +
            "})();";

        webView.evaluateJavascript(adBlockJS, null);
    }

    private void injectBackgroundPlayJS() {
        String bgPlayJS = "javascript:(function() {" +
            "if (typeof BackgroundPlay === 'undefined') {" +
            "   const script = document.createElement('script');" +
            "   script.src = 'https://cdn.jsdelivr.net/gh/gabrielwr/WebViewBackgroundPlay/backgroundPlay.js';" +
            "   document.head.appendChild(script);" +
            "}" +
            "})();";

        webView.evaluateJavascript(bgPlayJS, null);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
