package csumissu.fakewechat.base;

import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * @author sunyaxi
 * @date 2016/9/20
 */
public class BaseChildActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeEdge(64)
                .setSwipeRelateEnable(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
