package net.cherry;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.cherry.constants.Constants;
import net.cherry.retrofit.entities.DonateData;
import net.cherry.util.EmailUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class BillActivity extends AppCompatActivity {

    private ImageView iv_close;
    private TextView tv_userNm, tv_userSetleHstSn, tv_setleDt, tv_setleAmt, tv_setleAmt2, tv_mrhstNm, tv_bizrno, tv_cprNm, tv_zipAdres, tv_support;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        iv_close = findViewById(R.id.iv_close);
        tv_userNm = findViewById(R.id.tv_userNm);
        tv_userSetleHstSn = findViewById(R.id.tv_userSetleHstSn);
        tv_setleDt = findViewById(R.id.tv_setleDt);
        tv_setleAmt = findViewById(R.id.tv_setleAmt);
        tv_setleAmt2 = findViewById(R.id.tv_setleAmt2);
        tv_mrhstNm = findViewById(R.id.tv_mrhstNm);
        tv_bizrno = findViewById(R.id.tv_bizrno);
        tv_cprNm = findViewById(R.id.tv_cprNm);
        tv_zipAdres = findViewById(R.id.tv_zipAdres);
        tv_support = findViewById(R.id.tv_support);

        Intent intent = getIntent();
        DonateData bill = (DonateData) intent.getSerializableExtra("bill");

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Date 데이터형을 String 형으로 포맷시켜주기
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strDt = simpleDateFormat.format(bill.getSetleDt());

        //숫자 3자리마다 콤마 찍어주게 포맷시키기
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        String formatSetleAmt = decimalFormat.format(bill.getSetleAmt());

        tv_userNm.setText(bill.getUserNm());
        tv_userSetleHstSn.setText(String.valueOf(bill.getUserSetleHstSn()));
        tv_setleDt.setText(strDt);
        tv_setleAmt.setText(formatSetleAmt + "원");
        tv_setleAmt2.setText(formatSetleAmt + "원");
        tv_mrhstNm.setText(bill.getMrhstNm());
        tv_bizrno.setText(bill.getBizrno());
        tv_cprNm.setText(bill.getCprNm());
        tv_zipAdres.setText(bill.getZipAdres());

        tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailUtils.sendEmailToAdmin(BillActivity.this, "Cherry팀에게 메일보내기", new
                        String[]{"support@cherry.charity"});
            }
        });
    }
}
