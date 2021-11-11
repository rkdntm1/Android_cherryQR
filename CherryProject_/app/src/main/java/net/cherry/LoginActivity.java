package net.cherry;

import static net.cherry.constants.Constants.TAG;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import net.cherry.dialog.ProgressDialog;
import net.cherry.joinPage.JoinPage1Activity;
import net.cherry.retrofit.ApiClient;
import net.cherry.retrofit.RetrofitAPI;
import net.cherry.retrofit.entities.JoinDataInServer;
import net.cherry.util.SharedPreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 777;

    private SignInButton btn_google;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    private SharedPreferenceUtils spu;

    private ProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_google = findViewById(R.id.btn_google);

        btn_google.setOnClickListener(this);

        spu = new SharedPreferenceUtils(this);

        //회원가입 했는지 안했는지에 따라서 보이는 페이지가 달라지도록 설정(초기 false로 설정
        String userCheck = spu.getString(R.string.sp_user_check, null);

        Log.d(TAG, "(LoginActivity)현재 유저 check :: " + userCheck);

       if(userCheck.equals("noMember")) {
            Intent intent = new Intent(LoginActivity.this, JoinPage1Activity.class);
            startActivity(intent);
        }

        // == firebase 권한 가져오기 ==
        mAuth = FirebaseAuth.getInstance();

        //Google Login 옵션
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestIdToken("389111142924-2h4ncp1b3lm6g75mjo5jslvr91clmg2h.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Google Login 클래스
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Build the api client // 로그인 후 재 로그인시 계정 선택화면이 안뜨는 것을 해결하기 위해 추가
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "Login fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signOut();

        //로딩창 객체 생성
        customProgressDialog = new ProgressDialog(this);
        //로딩창을 투명하게
        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void signOut() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(mGoogleApiClient.isConnected()){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()) {
                                Log.d(TAG, "User Logged out");
                            }
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "google api client connection suspend");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_google:
                Log.d(TAG, "로그인버튼누름 현재 상태 ::" + spu.getString(R.string.sp_user_check, null));
                // 뒤로가기 누른 후 로그인 화면으로 다시 와서 로그인 할때 처리
                if(spu.getString(R.string.sp_user_check, null).equals("logIn")) {
                    finish();
                    Log.d(TAG, "로그인상태임");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TAG, "로그인상태아님");
                    Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(googleSignInIntent, RC_SIGN_IN);
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            // 구글 로그인 성공시 넘어오는 토큰값을 가지고 있는 Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "google get id ::" + account.getId());
                Log.d(TAG, "google get email ::" + account.getEmail());

                firebaseAuthWithGoogle(account.getIdToken()); // 토큰 값으로 firebase에 구글 권한 등록해주기
            } catch (ApiException e) {
                Log.d(TAG, "google login error ::" + e.toString());
            }
            Log.d(TAG, "Login callback 호출됨 ");

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential) // 구글 사용자 등록
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Log.d(TAG, "실패");
                        }

                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        Log.d(TAG, "uid:: " + uid);
                        spu.saveString(R.string.sp_user_uid, uid); // uid 값 저장
                        customProgressDialog.show();
                        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    customProgressDialog.cancel();
                                    String token = task.getResult().getToken();
                                    Log.d(TAG, "ID TOKEN ::" + token);
                                    spu.saveString(R.string.sp_user_token, token); // token값 저장

                                    //통신하기위해 정의해놓은 interface를 바탕으로 실제 사용할 클라이언트 객체를 생성
                                    RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

                                    //클라이언트 객체가 제공하는 enqueue 함수를 통해 통신에 대한 요청 및 응답처리 방법을 명시
                                    retrofitAPI.doCheckUser(spu.getString(R.string.sp_user_token, null)).enqueue(new Callback<JoinDataInServer>() {
                                        @Override
                                        public void onResponse(Call<JoinDataInServer> call, Response<JoinDataInServer> response) {
                                            Log.d(TAG, "response code :: " + response.code());
                                            // 가입이 된 상태에서는 response를 받아오는게 가능하지만
                                            // 가입되지 않은 상태에서는 response를 받아오지 못함.
                                            // 그러므로 받아오지 못할 때 noMember 상태로 변경해서 회원가입시키도록 만들자
                                            if(response.isSuccessful()) {
                                                JoinDataInServer data = response.body();
                                                String code = data.getCode();

                                                Log.d(TAG, "userCheck response 받기 성공");
                                                Log.d(TAG, "code ::" + code);
                                                if(code.equals("0000")) {
                                                    spu.saveString(R.string.sp_user_check, "logIn");
                                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Login에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    spu.saveString(R.string.sp_user_check, "noMember");
                                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "회원가입페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                spu.saveString(R.string.sp_user_check, "noMember");
                                            }
                                            recreate();
                                        }

                                        @Override
                                        public void onFailure(Call<JoinDataInServer> call, Throwable t) {
                                            Log.d(TAG, "userCheck error ::" + t.getMessage());
                                        }
                                    });
                                }
                            }
                        });

                    }
                });

    }
}