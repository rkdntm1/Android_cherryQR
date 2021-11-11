package net.cherry;

import static net.cherry.constants.Constants.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.cherry.donatePage.DonatePageAcitivity;
import net.cherry.history.HistroyActivity;
import net.cherry.util.QRUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_qr, iv_history, iv_setting;
    private String mrhId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        iv_qr = findViewById(R.id.iv_qr);
        iv_history = findViewById(R.id.iv_history);
        iv_setting = findViewById(R.id.iv_setting);


        // 버튼 클릭 이벤트 //
        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRUtils.startQRScan(MainActivity.this);
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistroyActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult ir = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if(data == null) {
                Log.e(TAG, "QR SCAN ERROR");
                Toast.makeText(MainActivity.this, "QR SCAN ERROR", Toast.LENGTH_SHORT).show();
            }else {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject(ir.getContents());
                            if (obj.getString("check").equals("cherryQR")) {
                                Toast.makeText(MainActivity.this, "QR 스캔 완료.", Toast.LENGTH_SHORT).show();
                                mrhId = obj.getString("mrhId");

//                                Intent intent = new Intent(MainActivity.this, DonatePageAcitivity.class);
//                                intent.putExtra("mrhId", mrhId);
//                                startActivity(intent);
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.8.56:18080/public/qrScreen/1"));
                                intent.putExtra("mrhId", mrhId);
                                startActivity(intent);

                            }else {
                                Toast.makeText(MainActivity.this, "잘못된 QR 코드입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1500);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}