package csumissu.fakewechat.common;

import android.content.Context;
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
 * @date 2016/6/28
 */
public class TileGapDelegate implements IAdapterDelegate {

    private Context mContext;
    private LayoutInflater mInflater;

    public TileGapDelegate(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewType() {
        return TileAdapter.TYPE_GAP;
    }

    @Override
    public boolean isForViewType(List items, int position) {
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
        boolean hasLabel = tileGap.labelResId > 0;
        if (hasLabel) {
            viewHolder.labelView.setText(tileGap.labelResId);
        } else {
            viewHolder.labelView.setText("");
        }
    }

    static class TileGapViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label)
        TextView labelView;

        public TileGapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class TileGap {
        public int labelResId;

        public TileGap() {
        }

        public TileGap(int resId) {
            labelResId = resId;
        }
    }
}
