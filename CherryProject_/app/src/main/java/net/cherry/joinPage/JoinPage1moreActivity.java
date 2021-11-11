package net.cherry.joinPage;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import net.cherry.R;

public class JoinPage1moreActivity extends AppCompatActivity {

    private WebView wv;
    private Button btn_finish;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page1more);

        wv = findViewById(R.id.wv);
        btn_finish = findViewById(R.id.btn_finish);

        url = getIntent().getStringExtra("url");

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//url 패턴에 따라 분기처리
                return super.shouldOverrideUrlLoading(view, url);
//                return falase; // 안드로이드 브라우저에서 계속 컨트롤러 하도록
//                return true;  //안드로이드 브라우저가 아닌 별도 처리 필요 할 떄
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
                Log.d(TAG, "url : " + url);
            }
        }); //기본 웹뷰 클라이언트

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 캐시 사용하지않게
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webSettings.setLoadWithOverviewMode(true); // 브라우저의 컨텐츠크기를 웹뷰에 맞게
        webSettings.setUseWideViewPort(true);      // 웹페이지 레이아웃을 디바이스 너비에 맞게 보여줌.

        wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // 쿠키
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(wv, true);

        wv.loadUrl(url);

        //@JavaScriptInterface

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if(appLinkData != null){
            Log.d(TAG, "APP LINK DATA : " + appLinkData.toString());
        }

    }
}