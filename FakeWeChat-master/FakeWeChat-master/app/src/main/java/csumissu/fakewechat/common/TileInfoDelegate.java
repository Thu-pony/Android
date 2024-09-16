package csumissu.fakewechat.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.User;

/**
 * @author sunyaxi
 * @date 2016/6/29
 */
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
        Glide.with(mContext).load(tileInfo.user.getAvatar())
                .placeholder(R.drawable.ic_photo_placeholder)
                .into(viewHolder.photoView);
        viewHolder.nameView.setText(tileInfo.user.getName());
        // viewHolder.uidView.setText(mContext.getString(R.string.uid, tileInfo.user.getUid()));
        viewHolder.uidView.setText(mContext.getString(R.string.email, "csumissu@icloud.com"));
    }

    static class TileInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView photoView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.uid)
        TextView uidView;

        public TileInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileInfo {

        User user;

        public TileInfo(User u) {
            user = u;
        }
    }
}
