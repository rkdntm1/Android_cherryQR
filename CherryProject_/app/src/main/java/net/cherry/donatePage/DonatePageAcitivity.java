package net.cherry.donatePage;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.cherry.MainActivity;
import net.cherry.R;
import net.cherry.retrofit.ApiClient;
import net.cherry.retrofit.RetrofitAPI;
import net.cherry.retrofit.entities.DonateDataInServer;
import net.cherry.retrofit.entities.DonateHistoryDataInServer;
import net.cherry.util.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DonatePageAcitivity extends AppCompatActivity {

    private WebView wv;
    private SharedPreferenceUtils spu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_page_acitivity);

        spu = new SharedPreferenceUtils(this);

        wv = findViewById(R.id.wv);

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
                String mrhId = getIntent().getStringExtra("mrhId");

                if(url.equals("http://192.168.8.56:18080/public/qrScreen/13")) {
                    Log.d(TAG, "QrScreen last url 일치");

                    RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);
                    retrofitAPI.doDonate(spu.getString(R.string.sp_user_token, null),
                            mrhId, 4900L).enqueue(new Callback<DonateDataInServer>() {
                        @Override
                        public void onResponse(Call<DonateDataInServer> call, Response<DonateDataInServer> response) {
                            Log.d(TAG, "response code ::" + response.code());
                            if (response.isSuccessful()) {
                                DonateDataInServer data = response.body();
                                Log.d(TAG, "donate response 받기 성공");
                                if (data.getCode().equals("0000")) { // 기부 성공
                                    //일단 기부 완료 후 3초 후 자동 이동하도록 설정
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(DonatePageAcitivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }, 3000);

                                } else {
                                    Log.d(TAG, "(donate) 오류로 인한 donate 실패 ");
                                }
                            } else {
                                Log.d(TAG, "(donate) response 못받아옴 ");
                            }
                        }

                        @Override
                        public void onFailure(Call<DonateDataInServer> call, Throwable t) {
                            Log.d(TAG, "(donate) response Error :: " + t.getMessage());
                        }
                    });
                }else {
                    Log.d(TAG, "url 불일치");
                }
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

        wv.loadUrl("http://192.168.8.56:18080/public/qrScreen/1");

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