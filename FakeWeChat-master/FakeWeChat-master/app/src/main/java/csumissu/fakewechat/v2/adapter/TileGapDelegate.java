package csumissu.fakewechat.v2.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class TileGapDelegate implements ITileTypeProvider {

    private Context mContext;
    private LayoutInflater mInflater;

    TileGapDelegate(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getViewType() {
        return TileGapDelegate.class.getSimpleName().hashCode();
    }

    @Override
    public boolean isForViewType(List<Object> items, int position) {
        return items.get(position) instanceof TileGap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new TileGapViewHolder(mInflater.inflate(R.layout.tile_gap, parent, false));
    }

    @Override
    public void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder) {
        TileGap tileGap = (TileGap) items.get(position);
        TileGapViewHolder viewHolder = (TileGapViewHolder) holder;
        if (tileGap.labelResId > 0) {
            viewHolder.labelView.setText(tileGap.labelResId);
            viewHolder.labelView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.labelView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {

    }

    static class TileGapViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label)
        TextView labelView;

        TileGapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileGap {
        @StringRes
        int labelResId;

        public TileGap() {
        }

        public TileGap(@StringRes int labelResId) {
            this.labelResId = labelResId;
        }

    }
}
