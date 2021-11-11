package net.cherry.history;

import static net.cherry.constants.Constants.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import net.cherry.R;
import net.cherry.dialog.ProgressDialog;
import net.cherry.retrofit.ApiClient;
import net.cherry.retrofit.RetrofitAPI;
import net.cherry.retrofit.entities.DonateData;
import net.cherry.retrofit.entities.DonateHistoryDataInServer;
import net.cherry.util.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistroyActivity extends AppCompatActivity {

    private List<DonateData> donateDataList;

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private Button btn_datePicker;

    private SharedPreferenceUtils spu;

    private Integer yearPick;
    private Integer monthPick;
    private Integer pageRef = 0;

    private ProgressDialog progressDialog; //로딩 다이얼 로그

    // 다이어로그에서 픽을 하면 listener가 불린다. 이때 나오는 year month값 이용하자
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.d(TAG, "year = " + year + ", month = " + month);
            yearPick = year;
            monthPick = month;

            progressDialog.show();

            RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

            retrofitAPI.doHistory(spu.getString(R.string.sp_user_token, null), pageRef, yearPick, monthPick)
                    .enqueue(new Callback<DonateHistoryDataInServer>() {
                        @Override
                        public void onResponse(Call<DonateHistoryDataInServer> call, Response<DonateHistoryDataInServer> response) {
                            Log.d(TAG, "(History) reponse code ::" + response.code());
                            if(response.isSuccessful()) {
                                progressDialog.cancel();
                                DonateHistoryDataInServer data = response.body();
                                String code = data.getCode();

                                Log.d(TAG, "(History) response 받기 성공");
                                Log.d(TAG, "code ::" + code);

                                donateDataList = data.getData(); // 받아온 데이터를 담아줌

                                adapter = new HistoryAdapter(donateDataList);
                                recyclerView.setAdapter(adapter);

                                if(code.equals("0000")) {
                                    Log.d(TAG, "(History) 정상적으로 결과물을 가져옴");
                                }else {
                                    Log.d(TAG, "(History) 리턴 코드 확인 요망 (정상 : 0000)");
                                }
                            }else {
                                Log.d(TAG, "(History) response를 정상적으로 받아오지 못함 ");
                            }
                        }

                        @Override
                        public void onFailure(Call<DonateHistoryDataInServer> call, Throwable t) {
                            Log.d(TAG, "(History) response error :: " + t.getMessage());
                        }
                    });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);

        donateDataList = new ArrayList<>();
        recyclerView = findViewById(R.id.rv);

        spu = new SharedPreferenceUtils(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager); //LayoutManager을 이용하여 RecyclerView 세팅

        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //날짜 선택 버튼
        btn_datePicker = findViewById(R.id.btn_datePicker);
        btn_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                net.cherry.dialog.DatePicker datePicker = new net.cherry.dialog.DatePicker();
                datePicker.setListener(listener);
                datePicker.show(getSupportFragmentManager(), "날짜 선택하기");
            }
        });

    }
}