package com.example.anyiassistor;

import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.List;




public class TileInfoDelegate implements IAdapterDelegate {

    private Context mContext;
    private LayoutInflater mInflater;

    public TileInfoDelegate(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewType() {
        return TileAdapter.TYPE_INFO;
    }

    @Override
    public boolean isForViewType(List items, int position) {
        return items.get(position) instanceof TileInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TileInfoViewHolder(mInflater.inflate(R.layout.tile_info, parent, false));
    }

    @Override
    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        TileInfo tileInfo = (TileInfo) items.get(position);
        TileInfoViewHolder viewHolder = (TileInfoViewHolder) holder;
        viewHolder.photoView.setImageResource(R.drawable.logo_img);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("phone",null);
        viewHolder.nameView.setText(username);
        // viewHolder.uidView.setText(mContext.getString(R.string.uid, tileInfo.user.getUid()));
        //viewHolder.uidView.setText(username);
    }

    static class TileInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoView = itemView.findViewById(R.id.icon);
        TextView nameView  = itemView.findViewById(R.id.name);
        TextView uidView = itemView.findViewById(R.id.uid);

        public TileInfoViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class TileInfo {

        User user;

        public TileInfo(User u) {
            user = u;
        }
        public TileInfo() {

        }
    }
}
