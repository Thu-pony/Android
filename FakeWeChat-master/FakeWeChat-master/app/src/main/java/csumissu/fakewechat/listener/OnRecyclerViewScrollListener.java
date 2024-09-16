package csumissu.fakewechat.listener;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

/**
 * @author sunyaxi
 * @date 2016/5/24
 */
public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = OnRecyclerViewScrollListener.class.getSimpleName();
    private static final boolean DEBUG = false;

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID,
    }

    /**
     * LayoutManager的类型(枚举)
     */
    protected LAYOUT_MANAGER_TYPE mLayoutManagerType;
    /**
     * 第一个可见的item的位置
     */
    private int mFirstVisibleItemPosition;
    /**
     * 最后一个可见的item的位置
     */
    private int mLastVisibleItemPosition;
    /**
     * 记录一个位置, 针对STAGGERED_GRID使用.
     */
    private int[] mTmpPositions;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (mLayoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used." +
                        " Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (mLayoutManagerType) {
            case GRID: {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                mFirstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
                mLastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                break;
            }
            case LINEAR: {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                mFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                mLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                break;
            }
            case STAGGERED_GRID: {
                StaggeredGridLayoutManager sgLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (mTmpPositions == null) {
                    mTmpPositions = new int[sgLayoutManager.getSpanCount()];
                }
                sgLayoutManager.findFirstVisibleItemPositions(mTmpPositions);
                mFirstVisibleItemPosition = findMin(mTmpPositions);
                sgLayoutManager.findLastVisibleItemPositions(mTmpPositions);
                mLastVisibleItemPosition = findMax(mTmpPositions);
                break;
            }
        }

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (DEBUG) {
            Log.v(TAG, "newState=" + newState + ", visibleItemCount=" + visibleItemCount
                    + ", totalItemCount=" + totalItemCount
                    + ", firstVisibleItemPosition=" + mFirstVisibleItemPosition
                    + ", lastVisibleItemPosition=" + mLastVisibleItemPosition);
        }
        if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mLastVisibleItemPosition >= totalItemCount - 1) {
                onBottom();
            } else if (mFirstVisibleItemPosition == 0) {
                onTop();
            }
        }
    }

    public abstract void onTop();

    public abstract void onBottom();

    private int findMin(int[] positions) {
        int min = positions[0];
        for (int value : positions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private int findMax(int[] positions) {
        int max = 0;
        for (int value : positions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
