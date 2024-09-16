package csumissu.fakewechat.comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.R;
import csumissu.fakewechat.data.Status;
import csumissu.fakewechat.listener.OnRecyclerViewScrollListener;

import static csumissu.fakewechat.util.Preconditions.checkNotNull;

/**
 * @author sunyaxi
 * @date 2016/5/23
 */
public class CommentsFragment extends RxFragment implements CommentsContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = CommentsFragment.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private CommentsContract.Presenter mPresenter;
    private CommentsAdapter mAdapter;
    private float mProgressViewOffset;

    private boolean mIsLoadingMore = false;
    private int mCurrentPage = 0;
    private static final int MAX_PAGE = 10;

    private Unbinder mUnbinder;

    public CommentsFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CommentsAdapter(getContext(), getFragmentManager());
        mProgressViewOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24, getResources().getDisplayMetrics());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void setPresenter(@NonNull CommentsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        // 第一次进入页面的时候显示加载进度条
        mRefreshLayout.setProgressViewOffset(false, 0, (int) mProgressViewOffset);
        mRefreshLayout.setColorSchemeResources(R.color.orange, R.color.teal,
                R.color.blue, R.color.brown);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.whiteSmoke)
                .sizeResId(R.dimen.divider)
                .build());
        mRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onTop() {
            }

            @Override
            public void onBottom() {
                if (mCurrentPage >= MAX_PAGE) {
                    Toast.makeText(getContext(), R.string.no_more_statuses, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mIsLoadingMore) {
                    mIsLoadingMore = true;
                    mRefreshLayout.setEnabled(false);
                    mPresenter.loadStatuses(++mCurrentPage);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void setRefreshIndicator(boolean active) {
        mRefreshLayout.setRefreshing(active);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showStatuses(List<Status> statuses) {
        if (mIsLoadingMore) {
            mAdapter.addData(statuses);
            mIsLoadingMore = false;
        } else {
            mAdapter.setData(statuses);
        }
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setEnabled(true);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(mRefreshLayout, message, Snackbar.LENGTH_LONG).show();
        mRefreshLayout.setEnabled(true);
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 0;
        mIsLoadingMore = false;
        mPresenter.loadAllStatuses();
    }
}
