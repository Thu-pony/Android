package com.example.anyiassistor;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TileMenuDelegate implements IAdapterDelegate {

    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;
    public TileMenuDelegate(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewType() {
        return TileAdapter.TYPE_MENU;
    }

    @Override
    public boolean isForViewType(List items, int position) {
        return items.get(position) instanceof TileMenu;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TileMenuViewHolder(mInflater.inflate(R.layout.tile_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        TileMenu tileMenu = (TileMenu) items.get(position);
        TileMenuViewHolder viewHolder = (TileMenuViewHolder) holder;
        viewHolder.iconView.setImageResource(tileMenu.iconResId);
        viewHolder.nameView.setText(tileMenu.nameResId);
        if (tileMenu.intent != null) {
            viewHolder.rootView.setOnClickListener(view -> {
                Log.d("AAAAAAAA","点击了");
                mContext.startActivity(tileMenu.intent);
            });
        }
    }

    static class TileMenuViewHolder extends RecyclerView.ViewHolder {
        View rootView = itemView.findViewById(R.id.container);
        ImageView iconView = itemView.findViewById(R.id.icon);
        TextView nameView =  itemView.findViewById(R.id.name);

        public TileMenuViewHolder(View itemView) {
            super(itemView);

        }
    }

    public static class TileMenu {
        public int iconResId;
        public int nameResId;
        public Intent intent;

        public TileMenu(int iconId, int nameId) {
            iconResId = iconId;
            nameResId = nameId;
        }


        public TileMenu(int iconId, int nameId, Intent i) {
            this(iconId, nameId);
            intent = i;
        }
    }
}
