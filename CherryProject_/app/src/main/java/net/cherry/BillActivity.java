package net.cherry;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import net.cherry.constants.Constants;
import net.cherry.retrofit.entities.DonateData;

import java.io.Serializable;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Intent intent = getIntent();
        DonateData bill = (DonateData) intent.getSerializableExtra("bill");
        Log.d(Constants.TAG, "test");
        Log.d(Constants.TAG, "" + bill.getMrhstDc());
    }
}