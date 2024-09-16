package csumissu.fakewechat.v2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.swipbackhelper.SwipeBackHelper;

import csumissu.fakewechat.R;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class BaseChildActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeEdge(getResources().getDimensionPixelSize(R.dimen.slide_switch_default_width))
                .setSwipeRelateEnable(true);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
