package net.cherry.joinPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import net.cherry.R;

public class JoinPage1Activity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox cb_all, cb_1, cb_2, cb_3;
    private TextView tv_1, tv_2, tv_3;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page1);

        cb_all = findViewById(R.id.cb_all);
        cb_1 = findViewById(R.id.cb_1);
        cb_2 = findViewById(R.id.cb_2);
        cb_3 = findViewById(R.id.cb_3);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        btn_next = findViewById(R.id.btn_next);

        btn_next.setEnabled(false);

        cb_all.setOnClickListener(this);
        cb_1.setOnClickListener(this);
        cb_2.setOnClickListener(this);
        cb_3.setOnClickListener(this);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        boolean isCheckedAll = cb_all.isChecked();

        switch (view.getId()) {
            case R.id.cb_all:
                cb_1.setChecked(isCheckedAll);
                cb_2.setChecked(isCheckedAll);
                cb_3.setChecked(isCheckedAll);
                break;
            case R.id.cb_1:
            case R.id.cb_2:
            case R.id.cb_3:
                checker();
                break;
            case R.id.tv_1:
                moveDetailPage("http://192.168.8.56:18080/public/terms/termsOfService");
                break;
            case R.id.tv_2:
                moveDetailPage("http://192.168.8.56:18080/public/terms/privacyPolicy");
                break;
            case R.id.tv_3:
                moveDetailPage("http://192.168.8.56:18080/public/terms/userNotice");
                break;
            case R.id.btn_next:
                finish();
                Intent intent = new Intent(this, JoinPage2Acitivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        if(cb_all.isChecked()) {
            btn_next.setEnabled(true);
        }else {
            btn_next.setEnabled(false);
        }
    }

    /**
     * 보기 버튼을 눌렀을 때 보여질 해당 url 파라미터로 넘겨 인텐트 시켜준다.
     * @param url
     */
    private void moveDetailPage(String url) {
        finish();
        Intent intent = new Intent(this, JoinPage1moreActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    /**
     * 체크박스 전체동의 체크 판단
     */
    private void checker() {
        if(cb_1.isChecked() && cb_2.isChecked() && cb_3.isChecked()) {
            cb_all.setChecked(true);
        }else {
            cb_all.setChecked(false);
        }
    }
}