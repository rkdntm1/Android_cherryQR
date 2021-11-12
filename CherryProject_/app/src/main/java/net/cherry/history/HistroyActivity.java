package net.cherry.history;

import static net.cherry.constants.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import net.cherry.R;
import net.cherry.constants.Constants;
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

    private List<DonateData> donateDataList = new ArrayList<>();

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private Button btn_datePicker;

    private SharedPreferenceUtils spu;

    private Integer yearPick;
    private Integer monthPick;
    private Integer pageRef = 0;

    private ProgressDialog progressDialog; //로딩 다이얼 로그
    
    private boolean isLoading = false;

    // 다이어로그에서 픽을 하면 listener가 불린다. 이때 나오는 year month값 이용하자
    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Log.d(TAG, "year = " + year + ", month = " + month);
            yearPick = year;
            monthPick = month;

            progressDialog.show();

            doHistory(pageRef, yearPick, monthPick);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);

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

        initScrollListener();


    }

    private void initScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d(TAG, "스크롤되나?");
                if(!isLoading) {
                    if(layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == donateDataList.size() - 1) {
                        Log.d(TAG, "마지막스크롤");
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        Log.d(TAG, "loadmore()");
        donateDataList.add(null);
        adapter.notifyItemInserted(donateDataList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                donateDataList.remove(donateDataList.size() - 1);
                int scrollPosition = donateDataList.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                doNextHistory(++pageRef, yearPick, monthPick);
            }
        }, 2000);
    }

    private void doHistory(Integer pageRef, Integer yearPick, Integer monthPick) {
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

                            if(code.equals("0000")) {
                                adapter = new HistoryAdapter(donateDataList);
                                recyclerView.setAdapter(adapter);
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
    private void doNextHistory(Integer pageRef, Integer yearPick, Integer monthPick) {
        RetrofitAPI retrofitAPI = ApiClient.getClient().create(RetrofitAPI.class);

        retrofitAPI.doHistory(spu.getString(R.string.sp_user_token, null), pageRef, yearPick, monthPick)
                .enqueue(new Callback<DonateHistoryDataInServer>() {
                    @Override
                    public void onResponse(Call<DonateHistoryDataInServer> call, Response<DonateHistoryDataInServer> response) {
                        Log.d(TAG, "(nextHistory) reponse code ::" + response.code());
                        if(response.isSuccessful()) {
                            progressDialog.cancel();
                            DonateHistoryDataInServer data = response.body();
                            String code = data.getCode();

                            Log.d(TAG, "(nextHistory) response 받기 성공");
                            Log.d(TAG, "code ::" + code);
                            List<DonateData> donDataData = data.getData();
                            for(DonateData donate: donDataData) {
                                donateDataList.add(donate); // 받아온 데이터를 담아줌
                            }
                            adapter.notifyDataSetChanged();
                            isLoading = false;

                        }else {
                            Log.d(TAG, "(nextHistory) response를 정상적으로 받아오지 못함 ");
                        }
                    }

                    @Override
                    public void onFailure(Call<DonateHistoryDataInServer> call, Throwable t) {
                        Log.d(TAG, "(nextHistory) response error :: " + t.getMessage());
                    }
                });
    }
}