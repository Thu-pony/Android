package csumissu.fakewechat.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;

/**
 * @author sunyaxi
 * @date 2016/6/28
 */
public class TileMenuDelegate implements IAdapterDelegate {

    private Context mContext;
    private LayoutInflater mInflater;

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
                mContext.startActivity(tileMenu.intent);
            });
        }
    }

    static class TileMenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container)
        View rootView;
        @BindView(R.id.icon)
        ImageView iconView;
        @BindView(R.id.name)
        TextView nameView;

        public TileMenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
