package csumissu.fakewechat.v2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class TileAdapter extends RecyclerView.Adapter {

    private List<Object> mItems = new ArrayList<>();
    private SparseArray<ITileTypeProvider> mAdapterMap = new SparseArray<>();

    public TileAdapter(Context context) {
        initTypeProviders(context);
    }

    public void setData(List<Object> items) {
        mItems.clear();
        if (items != null) {
            mItems.addAll(items);
        }
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < mAdapterMap.size(); i++) {
            ITileTypeProvider delegate = mAdapterMap.valueAt(i);
            if (delegate.isForViewType(mItems, position)) {
                return mAdapterMap.keyAt(i);
            }
        }
        throw new IllegalArgumentException("No delegate found for position " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ITileTypeProvider delegate = mAdapterMap.get(viewType);
        if (delegate != null) {
            return delegate.onCreateViewHolder(parent);
        }

        throw new IllegalArgumentException("No delegate found for position " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = holder.getItemViewType();
        ITileTypeProvider delegate = mAdapterMap.get(viewType);
        delegate.onBindViewHolder(mItems, position, holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        final int viewType = holder.getItemViewType();
        ITileTypeProvider delegate = mAdapterMap.get(viewType);
        delegate.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void initTypeProviders(Context context) {
        TileGapDelegate gapDelegate = new TileGapDelegate(context);
        mAdapterMap.put(gapDelegate.getViewType(), gapDelegate);
        TileMenuDelegate menuDelegate = new TileMenuDelegate(context);
        mAdapterMap.put(menuDelegate.getViewType(), menuDelegate);
        TileUserDelegate userDelegate = new TileUserDelegate(context);
        mAdapterMap.put(userDelegate.getViewType(), userDelegate);
        TileGroupDelegate groupDelegate = new TileGroupDelegate(context);
        mAdapterMap.put(groupDelegate.getViewType(), groupDelegate);
    }
}
