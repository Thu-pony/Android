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
import rx.Subscription;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class TileMenuDelegate implements ITileTypeProvider {

    private Context mContext;
    private LayoutInflater mInflater;

    TileMenuDelegate(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getViewType() {
        return TileMenuDelegate.class.getSimpleName().hashCode();
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
        if (tileMenu.iconResId > 0) {
            viewHolder.iconView.setImageResource(tileMenu.iconResId);
            viewHolder.iconView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.iconView.setVisibility(View.GONE);
        }
        viewHolder.nameView.setText(tileMenu.nameResId);
        viewHolder.badgeView.setText(String.valueOf(tileMenu.count));
        viewHolder.badgeView.setVisibility(tileMenu.count > 0 ? View.VISIBLE : View.INVISIBLE);
        Subscription subscription = RxView.clicks(viewHolder.rootView)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (tileMenu.intent != null) {
                        mContext.startActivity(tileMenu.intent);
                    } else if (tileMenu.runnable != null) {
                        tileMenu.runnable.run();
                    }
                });
        viewHolder.rootView.setTag(subscription);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        TileMenuViewHolder viewHolder = (TileMenuViewHolder) holder;
        Subscription subscription = (Subscription) viewHolder.rootView.getTag();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    static class TileMenuViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container)
        View rootView;
        @BindView(R.id.icon)
        ImageView iconView;
        @BindView(R.id.name)
        TextView nameView;
        @BindView(R.id.badge)
        TextView badgeView;

        TileMenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileMenu {
        int iconResId;
        int nameResId;
        int count;
        Intent intent;
        Runnable runnable;

        private TileMenu() {
        }

        public static class Builder {
            private TileMenu tileMenu = new TileMenu();

            public Builder setIconRes(@DrawableRes int iconRes) {
                tileMenu.iconResId = iconRes;
                return this;
            }

            public Builder setNameRes(@StringRes int nameRes) {
                tileMenu.nameResId = nameRes;
                return this;
            }

            public Builder setUnreadCount(int count) {
                tileMenu.count = count;
                return this;
            }

            public Builder setIntent(Intent intent) {
                tileMenu.intent = intent;
                return this;
            }

            public Builder setAction(Runnable runnable) {
                tileMenu.runnable = runnable;
                return this;
            }

            public TileMenu create() {
                if (tileMenu.nameResId <= 0) {
                    throw new IllegalArgumentException("invalid name resource id");
                }
                return tileMenu;
            }

        }
    }
}
