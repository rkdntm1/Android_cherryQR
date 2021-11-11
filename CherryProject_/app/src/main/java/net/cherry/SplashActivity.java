package net.cherry;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import net.cherry.util.SharedPreferenceUtils;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferenceUtils spu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spu = new SharedPreferenceUtils(this);
        spu.saveString(R.string.sp_user_check, "logOut"); // 맨처음 스플래쉬화면 진입시 회원가입 상태가 풀리도록 설정
        Log.d(TAG, "유저 check in Splash :: " + spu.getString(R.string.sp_user_check, "후"));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, 3000); // 3초 후 메인 엑티비티로 이동
    }

    private void openMainActivity() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}