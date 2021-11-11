package net.cherry.javascript;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import net.cherry.constants.Constants;

public class JavascriptInterface {
    Context context;
    WebView webView;
    Activity activity;

    public JavascriptInterface(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
        this.activity = (Activity) context;
    }

    @android.webkit.JavascriptInterface // javascript 와 연결시킬때
    public void getJoinData1(String mobIphonNo, String brthNum, String sexSeCode) {
        Log.d(Constants.TAG, "mobIphonNo : " + mobIphonNo + ",brthNum : " + brthNum + ", sexSeCode : " + sexSeCode);
    }
}
