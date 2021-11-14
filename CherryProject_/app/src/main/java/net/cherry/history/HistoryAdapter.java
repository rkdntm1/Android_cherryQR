package net.cherry.history;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Context;
import android.content.Intent;
import android.media.tv.TvContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.Constants;

import net.cherry.BillActivity;
import net.cherry.R;
import net.cherry.retrofit.entities.DonateData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<DonateData> dataList;
    private Context context;

    public HistoryAdapter(List<DonateData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.history_element, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }else {
            Log.d(TAG, "recyclerView onCreate 확인");
            View view = inflater.inflate(R.layout.item_loading, parent, false);
            context = parent.getContext();
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DonateData donateData = dataList.get(position);

        switch (getItemViewType(position)) {
            case VIEW_TYPE_ITEM:
                ViewHolder viewHolder = (ViewHolder) holder;

                //Date 데이터형을 String 형으로 포맷시켜주기
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String strDt = simpleDateFormat.format(donateData.getFrstRegistDt());

                //숫자 3자리마다 콤마 찍어주게 포맷시키기
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                String formatSetleAmt = decimalFormat.format(donateData.getSetleAmt());

                Log.d(TAG, "strDT ::" + strDt);
                viewHolder.tv_userSetleHstSn.setText(String.valueOf(donateData.getUserSetleHstSn()));
                viewHolder.tv_date.setText(strDt);
                viewHolder.tv_foundation.setText("[" + donateData.getMrhstNm() + "]");
                viewHolder.tv_title.setText(donateData.getMrhstDc());
                viewHolder.tv_price.setText(formatSetleAmt + "원");
                break;
            case VIEW_TYPE_LOADING:
                showLoadingView((LoadingViewHolder) holder, position);
                break;
            default:
                break;
        }
    }
    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
    }
    /**
     * View Holder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_userSetleHstSn;
        TextView tv_date;       //기부 날짜
        TextView tv_foundation; //기부 단체
        TextView tv_title;      //기부 제목
        TextView tv_price;      //기부 금액

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_userSetleHstSn = itemView.findViewById(R.id.tv_userSetleHstSn);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_foundation = itemView.findViewById(R.id.tv_foundation);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_price = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //item 목록을 클릭시 해당 위치에 있는 item의 데이터 객체를 intent에 담아
                        //영수증 페이지에 보내줌
                        DonateData donateDataByPos = dataList.get(pos);
                        Intent intent = new Intent(view.getContext(), BillActivity.class);
                        intent.putExtra("bill", donateDataByPos);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
