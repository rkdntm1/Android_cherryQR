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

        //???????????? ????????? ??????????????? ????????? ????????? ???????????? ??????????????? ??????(?????? false??? ??????
        String userCheck = spu.getString(R.string.sp_user_check, null);

        Log.d(TAG, "(LoginActivity)?????? ?????? check :: " + userCheck);

       if(userCheck.equals("noMember")) {
            Intent intent = new Intent(LoginActivity.this, JoinPage1Activity.class);
            startActivity(intent);
        }

        // == firebase ?????? ???????????? ==
        mAuth = FirebaseAuth.getInstance();

        //Google Login ??????
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestIdToken("389111142924-2h4ncp1b3lm6g75mjo5jslvr91clmg2h.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Google Login ?????????
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Build the api client // ????????? ??? ??? ???????????? ?????? ??????????????? ????????? ?????? ???????????? ?????? ??????
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

        //????????? ?????? ??????
        customProgressDialog = new ProgressDialog(this);
        //???????????? ????????????
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
                Log.d(TAG, "????????????????????? ?????? ?????? ::" + spu.getString(R.string.sp_user_check, null));
                // ???????????? ?????? ??? ????????? ???????????? ?????? ?????? ????????? ?????? ??????
                if(spu.getString(R.string.sp_user_check, null).equals("logIn")) {
                    finish();
                    Log.d(TAG, "??????????????????");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TAG, "?????????????????????");
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
            // ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "google get id ::" + account.getId());
                Log.d(TAG, "google get email ::" + account.getEmail());

                firebaseAuthWithGoogle(account.getIdToken()); // ?????? ????????? firebase??? ?????? ?????? ???????????????
            } catch (ApiException e) {
                Log.d(TAG, "google login error ::" + e.toString());
            }
            Log.d(TAG, "Login callback ????????? ");

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential) // ?????? ????????? ??????
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Log.d(TAG, "??????");
                        }

                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        Log.d(TAG, "uid:: " + uid);
                        spu.saveString(R.string.sp_user_uid, uid); // uid ??? ??????
                        customProgressDialog.show();
                        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                            @Override
                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                if (task.isSuccessful()) {
                                    customProgressDialog.cancel();
                                    String token = task.getResult().getToken();
                                    Log.d(TAG, "ID TOKEN ::" + token);
                                    spu.saveString(R.string.sp_user_token, token); // token??? ??????

                                    //?????????????????? ??????????????? interface??? ???????????? ?????? ????????? ??????????????? ????????? ??????
                                    RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

                                    //??????????????? ????????? ???????????? enqueue ????????? ?????? ????????? ?????? ?????? ??? ???????????? ????????? ??????
                                    retrofitAPI.doCheckUser(spu.getString(R.string.sp_user_token, null)).enqueue(new Callback<JoinDataInServer>() {
                                        @Override
                                        public void onResponse(Call<JoinDataInServer> call, Response<JoinDataInServer> response) {
                                            Log.d(TAG, "response code :: " + response.code());
                                            // ????????? ??? ??????????????? response??? ??????????????? ???????????????
                                            // ???????????? ?????? ??????????????? response??? ???????????? ??????.
                                            // ???????????? ???????????? ?????? ??? noMember ????????? ???????????? ???????????????????????? ?????????
                                            if(response.isSuccessful()) {
                                                JoinDataInServer data = response.body();
                                                String code = data.getCode();

                                                Log.d(TAG, "userCheck response ?????? ??????");
                                                Log.d(TAG, "code ::" + code);
                                                if(code.equals("0000")) {
                                                    spu.saveString(R.string.sp_user_check, "logIn");
                                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Login??? ??????????????????.", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    spu.saveString(R.string.sp_user_check, "noMember");
                                                    Toast.makeText(LoginActivity.this.getApplicationContext(), "???????????????????????? ???????????????.", Toast.LENGTH_SHORT).show();
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