package csumissu.fakewechat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ActionProvider;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import csumissu.fakewechat.R;
import csumissu.fakewechat.main.PlusActionProvider;

/**
 * @author sunyaxi
 * @date 2016/6/29
 */
public class MenuActionView extends ViewGroup implements PopupWindow.OnDismissListener,
        AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.image)
    ImageView mIcon;
    @BindView(R.id.view_content)
    FrameLayout mContent;
    /**
     * The ActionProvider hosting this view, if applicable.
     */
    ActionProvider mProvider;
    /**
     * Popup window for showing the activity overflow list.
     */
    private ListPopupWindow mListPopupWindow;
    /**
     * Flag whether this view is attached to a window.
     */
    private boolean mIsAttachedToWindow;
    /**
     * The maximal width of the list popup.
     */
    private final int mListPopupMaxWidth;
    /**
     * Listener for the dismissal of the popup/alert.
     */
    private PopupWindow.OnDismissListener mOnDismissListener;

    private MenuActionViewAdapter mAdapter;
    private List<PlusActionProvider.Data> mDataSource;

    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (isShowingPopup()) {
                if (!isShown()) {
                    getListPopupWindow().dismiss();
                } else {
                    getListPopupWindow().show();
                    if (mProvider != null) {
                        mProvider.subUiVisibilityChanged(true);
                    }
                }
            }
        }
    };

    public MenuActionView(Context context) {
        this(context, null);
    }

    public MenuActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.menu_action_view, this, true);
        ButterKnife.bind(this, rootView);
        mContent.setOnClickListener(this);
        mContent.setOnTouchListener(new ForwardingListener(mContent) {
            @Override
            public ShowableListMenu getPopup() {
                return getListPopupWindow();
            }

            @Override
            protected boolean onForwardingStarted() {
                showPopup();
                return true;
            }

            @Override
            protected boolean onForwardingStopped() {
                dismissPopup();
                return true;
            }
        });
        mAdapter = new MenuActionViewAdapter();

        Resources resources = context.getResources();
        mListPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2,
                resources.getDimensionPixelSize(R.dimen.config_prefDialogWidth));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        measureChild(mContent, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mContent.getMeasuredWidth(), mContent.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContent.layout(0, 0, r - l, b - t);
        if (!isShowingPopup()) {
            dismissPopup();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
        if (isShowingPopup()) {
            dismissPopup();
        }
        mIsAttachedToWindow = false;
    }

    public void setIcon(int iconResId) {
        mIcon.setImageResource(iconResId);
    }

    public void setTitle(int titleResId) {
        mContent.setContentDescription(getContext().getString(titleResId));
    }

    public void setDataSource(List<PlusActionProvider.Data> dataSource) {
        mDataSource = dataSource;
    }

    /**
     * Set the provider hosting this view, if applicable.
     *
     * @hide Internal use only
     */
    public void setProvider(ActionProvider provider) {
        mProvider = provider;
    }

    /**
     * Sets a listener to receive a callback when the popup is dismissed.
     *
     * @param listener The listener to be notified.
     */
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /**
     * Shows the popup window with activities.
     *
     * @return True if the popup was shown, false if already showing.
     */
    public boolean showPopup() {
        if (isShowingPopup() || !mIsAttachedToWindow) {
            return false;
        }
        showPopupUnchecked();
        return true;
    }

    /**
     * Shows the popup no matter if it was already showing.
     */
    private void showPopupUnchecked() {
        getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        ListPopupWindow popupWindow = getListPopupWindow();
        if (!popupWindow.isShowing()) {
            final int contentWidth = Math.min(mAdapter.measureContentWidth(), mListPopupMaxWidth);
            popupWindow.setContentWidth(contentWidth);
            popupWindow.show();
            if (mProvider != null) {
                mProvider.subUiVisibilityChanged(true);
            }
        }
    }

    /**
     * Dismisses the popup window with activities.
     *
     * @return True if dismissed, false if already dismissed.
     */
    public boolean dismissPopup() {
        if (isShowingPopup()) {
            getListPopupWindow().dismiss();
            ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        }
        return true;
    }

    /**
     * Gets whether the popup window with activities is shown.
     *
     * @return True if the popup is shown.
     */
    public boolean isShowingPopup() {
        return getListPopupWindow().isShowing();
    }

    /**
     * Gets the list popup window which is lazily initialized.
     *
     * @return The popup.
     */
    private ListPopupWindow getListPopupWindow() {
        if (mListPopupWindow == null) {
            mListPopupWindow = new ListPopupWindow(getContext());
            mListPopupWindow.setAdapter(mAdapter);
            mListPopupWindow.setAnchorView(MenuActionView.this);
            mListPopupWindow.setModal(true);
            mListPopupWindow.setOnItemClickListener(this);
            mListPopupWindow.setOnDismissListener(this);
        }
        return mListPopupWindow;
    }

    @Override
    public void onDismiss() {
        notifyOnDismissListener();
        if (mProvider != null) {
            mProvider.subUiVisibilityChanged(false);
        }
    }

    private void notifyOnDismissListener() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_content:
                showPopupUnchecked();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismissPopup();
        if (mDataSource != null) {
            Runnable callback = mDataSource.get(position).callback;
            if (callback != null) {
                callback.run();
            }
        }
    }

    private class MenuActionViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDataSource != null ? mDataSource.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return mDataSource != null ? mDataSource.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.menu_action_view_list_item, parent, false);
            }
            PlusActionProvider.Data item = mDataSource.get(position);
            ((ImageView) convertView.findViewById(R.id.icon)).setImageResource(item.icon);
            ((TextView) convertView.findViewById(R.id.title)).setText(item.title);
            return convertView;
        }

        public int measureContentWidth() {
            int contentWidth = 0;
            View itemView = null;

            final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            final int count = getCount();

            for (int i = 0; i < count; i++) {
                itemView = getView(i, itemView, null);
                itemView.measure(widthMeasureSpec, heightMeasureSpec);
                contentWidth = Math.max(contentWidth, itemView.getMeasuredWidth());
            }
            return contentWidth;
        }
    }
}
