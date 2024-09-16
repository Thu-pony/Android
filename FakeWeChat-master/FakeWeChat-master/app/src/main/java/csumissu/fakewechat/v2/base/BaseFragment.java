package csumissu.fakewechat.v2.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxFragment;

import csumissu.fakewechat.v2.AppComponent;
import csumissu.fakewechat.v2.WechatApp;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class BaseFragment extends RxFragment {

    protected AppComponent getApplicationComponent() {
        return ((WechatApp) getActivity().getApplication()).component();
    }

    protected void showMessage(@StringRes int msgId) {
        showMessage(getString(msgId));
    }

    protected void showMessage(@NonNull String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
