package csumissu.fakewechat.v2.adapter;

import android.content.Context;
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
import csumissu.fakewechat.v2.bean.User;
import csumissu.fakewechat.v2.util.ImageLoader;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class TileUserDelegate implements ITileTypeProvider {

    private Context mContext;
    private LayoutInflater mInflater;

    TileUserDelegate(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getViewType() {
        return TileUserDelegate.class.getSimpleName().hashCode();
    }

    @Override
    public boolean isForViewType(List items, int position) {
        return items.get(position) instanceof TileUser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TileInfoViewHolder(mInflater.inflate(R.layout.tile_user, parent, false));
    }

    @Override
    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        TileUser tileUser = (TileUser) items.get(position);
        TileInfoViewHolder viewHolder = (TileInfoViewHolder) holder;

        ImageLoader.loadImage(mContext, viewHolder.photoView, tileUser.user.getAvatarUrl(),
                R.drawable.ic_photo_placeholder);
        viewHolder.nameView.setText(tileUser.user.getName());
        viewHolder.signatureView.setText(mContext.getString(R.string.email, "csumissu@icloud.com"));
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    static class TileInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photoView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.signature)
        TextView signatureView;

        TileInfoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileUser {

        User user;

        public TileUser(User u) {
            user = u;
        }
    }
}
