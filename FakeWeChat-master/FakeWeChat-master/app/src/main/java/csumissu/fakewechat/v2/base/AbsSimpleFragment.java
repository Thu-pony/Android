package csumissu.fakewechat.v2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.adapter.TileAdapter;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public abstract class AbsSimpleFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private Unbinder mUnbinder;
    private TileAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lay_refresh_recycle_view, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mRefreshLayout.setColorSchemeResources(R.color.orange, R.color.teal,
                R.color.blue, R.color.brown);
        mRefreshLayout.setEnabled(false);

        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.whiteSmoke)
                .sizeResId(R.dimen.divider)
                .build());

        mAdapter = new TileAdapter(getContext());
        mAdapter.setData(loadData());

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    protected void refreshDate() {
        mAdapter.setData(loadData());
        mAdapter.notifyDataSetChanged();
    }

    protected void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshLayout.setOnRefreshListener(listener);
        mRefreshLayout.setEnabled(listener != null);
    }

    protected void stopRefreshing() {
        if (mRefreshLayout != null) {
            if (mRefreshLayout.isEnabled()) {
                mRefreshLayout.setRefreshing(false);
            }
        }
    }

    protected abstract List<Object> loadData();

}