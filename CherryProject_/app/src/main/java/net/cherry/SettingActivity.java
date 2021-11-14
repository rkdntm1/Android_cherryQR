package net.cherry;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;

import net.cherry.joinPage.JoinPage1moreActivity;
import net.cherry.retrofit.ApiClient;
import net.cherry.retrofit.RetrofitAPI;
import net.cherry.retrofit.entities.JoinDataInServer;
import net.cherry.settingPage.PwPageViewActivity;
import net.cherry.util.EmailUtils;
import net.cherry.util.SharedPreferenceUtils;

import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_pwChange, tv_acManager, tv_preview1, tv_preview2, tv_preview3, tv_supEmail, tv_signOut;

    private SharedPreferenceUtils spu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_pwChange = findViewById(R.id.tv_pwChange);
        tv_acManager = findViewById(R.id.tv_acManager);
        tv_preview1 = findViewById(R.id.tv_preview1);
        tv_preview2 = findViewById(R.id.tv_preview2);
        tv_preview3 = findViewById(R.id.tv_preview3);
        tv_supEmail = findViewById(R.id.tv_supEmail);
        tv_signOut = findViewById(R.id.tv_signOut);

        spu = new SharedPreferenceUtils(this);

        tv_pwChange.setOnClickListener(this);
        tv_acManager.setOnClickListener(this);
        tv_preview1.setOnClickListener(this);
        tv_preview2.setOnClickListener(this);
        tv_preview3.setOnClickListener(this);
        tv_supEmail.setOnClickListener(this);
        tv_signOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pwChange:
                Log.d(TAG, "눌렸나");
                Intent intent = new Intent(SettingActivity.this, PwPageViewActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_acManager:
                moveDetailPage("http://192.168.8.56:18080/public/accountScreen");
                break;
            case R.id.tv_preview1:
                moveDetailPage("http://192.168.8.56:18080/public/terms/termsOfService");
                break;
            case R.id.tv_preview2:
                moveDetailPage("http://192.168.8.56:18080/public/terms/privacyPolicy");
                break;
            case R.id.tv_preview3:
                moveDetailPage("http://192.168.8.56:18080/public/terms/userNotice");
                break;
            case R.id.tv_supEmail:
                EmailUtils.sendEmailToAdmin(SettingActivity.this, "Cherry팀에게 메일보내기", new
                        String[]{"support@cherry.charity"});
                break;
            case R.id.tv_signOut:
                signOut();
                break;
            default:
                break;
        }
    }

    private void signOut() {
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

        retrofitAPI.doWithdrawal(spu.getString(R.string.sp_user_token, null)).enqueue(new Callback<JoinDataInServer>() {
            @Override
            public void onResponse(Call<JoinDataInServer> call, Response<JoinDataInServer> response) {
                if(response.isSuccessful()) {
                    // 회원 탈퇴시 유저상태를 로그아웃 상태로 바꾸며 다시 LoginActivity로 되돌아가도록 설정
                    JoinDataInServer data = response.body();
                    Log.d(TAG, "withdrawal response 받기 성공");
                    if(data.getCode().equals("0000")) {
                        spu.saveString(R.string.sp_user_check, "logOut");
                        Toast.makeText(SettingActivity.this.getApplicationContext(), "회원 탈퇴 완료", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Log.d(TAG, "response로 받은 Code 확인 요망");
                    }
                }else {
                    Log.d(TAG, "withdrawal response 받기 실패");
                }
            }

            @Override
            public void onFailure(Call<JoinDataInServer> call, Throwable t) {
                Log.d(TAG, "withdrawal error ::" + t.getMessage());
            }
        });
    }

    /**
     * 해당 url의 웹뷰로 이동
     * @param url
     */
    private void moveDetailPage(String url) {
        Intent intent = new Intent(this, JoinPage1moreActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}