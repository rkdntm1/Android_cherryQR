package net.cherry.history;

import static com.google.firebase.messaging.Constants.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.cherry.BillActivity;
import net.cherry.R;
import net.cherry.retrofit.entities.DonateData;

import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DonateData> dataList;
    private Context context;

    public HistoryAdapter(List<DonateData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.history_element, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DonateData donateData = dataList.get(position);

        ViewHolder viewHolder = (ViewHolder) holder;

        //Date 데이터형을 String 형으로 포맷시켜주기
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String strDt = simpleDateFormat.format(donateData.getFrstRegistDt());
        Log.d(TAG, "strDT ::" + strDt);
        viewHolder.tv_date.setText(strDt);
        viewHolder.tv_foundation.setText("[" + donateData.getMrhstNm() + "]");
        viewHolder.tv_title.setText(donateData.getMrhstDc());
        viewHolder.tv_price.setText(String.valueOf(donateData.getSetleAmt()) + "원");

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    /**
     * View Holder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date;       //기부 날짜
        TextView tv_foundation; //기부 단체
        TextView tv_title;      //기부 제목
        TextView tv_price;      //기부 금액

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.tv_foundation = itemView.findViewById(R.id.tv_foundation);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_price = itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        DonateData donateDataByPos = dataList.get(pos);
                        Intent intent = new Intent(view.getContext(), BillActivity.class);
                        intent.putExtra("bill", donateDataByPos);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
