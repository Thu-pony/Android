package com.example.anyiassistor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.CardViewHolder> {

    private List<Carditem> mList;

    // 构造函数，传入数据
    public BankAdapter(List<Carditem> data) {
        this.mList = data;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 使用LayoutInflater来加载card_item.xml布局
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        // 绑定数据到布局中
        Carditem carditem = mList.get(position);
        String bank_name = carditem.getBank_name();
        holder.bank_name.setText(bank_name);
        String card_type = carditem.getCard_type();
        holder.card_type.setText(card_type);
        String card_num = carditem.getCard_num();
        holder.card_num.setText("**** **** **** " + card_num.substring(card_num.length() - 4));
    }

    @Override
    public int getItemCount() {
        // 返回数据集的长度
        return mList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView bank_name;
        TextView card_type;
        TextView card_num;
        CardViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            bank_name = itemView.findViewById(R.id.bank_name);
            card_type = itemView.findViewById(R.id.card_type);
            card_num = itemView.findViewById(R.id.card_num);
        }
    }
}

