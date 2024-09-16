package csumissu.fakewechat.v2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.ui.group.GroupActivity;
import rx.Subscription;


/**
 * @author sunyaxi
 * @date 2016/11/20.
 */
public class TileGroupDelegate implements ITileTypeProvider {

    private Context mContext;
    private LayoutInflater mInflater;

    TileGroupDelegate(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewType() {
        return TileGroupDelegate.class.getSimpleName().hashCode();
    }

    @Override
    public boolean isForViewType(List items, int position) {
        return items.get(position) instanceof TileGroup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new GroupViewHolder(mInflater.inflate(R.layout.tile_group, parent, false));
    }

    @Override
    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        TileGroup tileGroup = (TileGroup) items.get(position);
        GroupViewHolder viewHolder = (GroupViewHolder) holder;

        viewHolder.photoView.setImageResource(tileGroup.photoResId);
        viewHolder.nameView.setText(tileGroup.nameResId);
        viewHolder.badgeView.setVisibility(View.GONE);
        viewHolder.timeView.setVisibility(View.GONE);
        viewHolder.tipView.setVisibility(View.GONE);

        Subscription subscription = RxView.clicks(viewHolder.containerView)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    Intent intent = new Intent(mContext, GroupActivity.class);
                    mContext.startActivity(intent);
                });
        viewHolder.containerView.setTag(subscription);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        GroupViewHolder viewHolder = (GroupViewHolder) holder;
        Subscription subscription = (Subscription) viewHolder.containerView.getTag();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photoView;
        @BindView(R.id.badge)
        TextView badgeView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.tip)
        TextView tipView;
        @BindView(R.id.time)
        TextView timeView;
        @BindView(R.id.container)
        View containerView;

        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileGroup {
        @DrawableRes
        int photoResId;
        @StringRes
        int nameResId;

        public TileGroup(@DrawableRes int photoResId, @StringRes int nameResId) {
            this.photoResId = photoResId;
            this.nameResId = nameResId;
        }
    }
}
