package net.cherry.joinPage;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.cherry.LoginActivity;
import net.cherry.R;
import net.cherry.SettingActivity;
import net.cherry.StartActivity;
import net.cherry.javascript.JavascriptInterface;
import net.cherry.retrofit.ApiClient;
import net.cherry.retrofit.RetrofitAPI;
import net.cherry.retrofit.entities.JoinDataInServer;
import net.cherry.util.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinPage3Activity extends AppCompatActivity {

    private WebView wv;
    private String saveUrl;

    private SharedPreferenceUtils spu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        spu = new SharedPreferenceUtils(this);

        wv = findViewById(R.id.wv);

        wv.setWebChromeClient(new WebChromeClient());
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
                Log.d(TAG, "페이지 로딩완료. 현 url : " + url);

                if(url.substring(url.length() - 1).equals("5")) {
                    Log.d(TAG, "(joinPage) last url 일치. 유저 상태 logIn으로 변경");
                    // 가입 페이지 완료 시 유저 상태 logIn 상태로 변경해줌
                    login();
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

        wv.loadUrl("http://192.168.8.56:18080/public/joinScreen/1");

        //@JavaScriptInterface
        wv.addJavascriptInterface(new JavascriptInterface(this, wv), "Android");

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

        if(appLinkData != null){
            Log.d(TAG, "APP LINK DATA : " + appLinkData.toString());
        }


    }

    private void login() {
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

        retrofitAPI.doJoin(spu.getString(R.string.sp_user_uid, null), spu.getString(R.string.sp_user_token, null),null, JavascriptInterface.brthNum,JavascriptInterface.sexSeCode,null,JavascriptInterface.mobIphonNo)
                .enqueue(new Callback<JoinDataInServer>() {
                    @Override
                    public void onResponse(Call<JoinDataInServer> call, Response<JoinDataInServer> response) {
                        if(response.isSuccessful()) {
                            // 0000:회원가입 성공시 상태를 login 상태로 변경시키며 StartActivity화면으로 이동.
                            // 0001:이미 등록된 유저 였을 시 상태를 LogOut 상태로 변경시키고 LoginActivity 화면으로 이동.
                            JoinDataInServer data = response.body();
                            Log.d(TAG, "Join response 받기 성공");
                            if(data.getCode().equals("0000")) {
                                spu.saveString(R.string.sp_user_check, "logIn");
                                Toast.makeText(JoinPage3Activity.this.getApplicationContext(), "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                moveStartActivity();
                            }else if(data.getCode().equals("0001")) {
                                spu.saveString(R.string.sp_user_check, "logOut");
                                Toast.makeText(JoinPage3Activity.this.getApplicationContext(), "이미 등록된 회원입니다.", Toast.LENGTH_SHORT).show();
                                moveLoginActivity();
                            }
                        }else {
                            //response를 못받았을시에도 다시 login 화면으로 이동
                            Log.d(TAG, "Join response 받기 실패");
                            Toast.makeText(JoinPage3Activity.this.getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                            moveLoginActivity();
                        }
                    }

                    //오류 발생시에도 login 화면으로 이동
                    @Override
                    public void onFailure(Call<JoinDataInServer> call, Throwable t) {
                        Log.d(TAG, "Join Error ::" + t.getMessage());
                        Toast.makeText(JoinPage3Activity.this.getApplicationContext(), "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        moveLoginActivity();
                    }

                    private void moveStartActivity() {
                        Intent intent = new Intent(JoinPage3Activity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    private void moveLoginActivity() {
                        Intent intent = new Intent(JoinPage3Activity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}