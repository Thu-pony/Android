package csumissu.fakewechat.main;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.SubMenu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import csumissu.fakewechat.R;
import csumissu.fakewechat.widget.MenuActionView;

/**
 * @author sunyaxi
 * @date 2016/6/29
 */
public class PlusActionProvider extends ActionProvider {

    private Context mContext;
    private List<Data> mDataSource;

    public PlusActionProvider(Context context) {
        super(context);
        mContext = context;
        mDataSource = getDefaultDataSource();
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }

    @Override
    public View onCreateActionView() {
        MenuActionView menuActionView = new MenuActionView(mContext);
        menuActionView.setProvider(this);
        menuActionView.setIcon(R.drawable.ic_add);
        menuActionView.setTitle(R.string.add_more);
        menuActionView.setDataSource(mDataSource);
        return menuActionView;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        if (mDataSource == null) {
            return;
        }
        for (Data data : mDataSource) {
            subMenu.add(mContext.getString(data.title))
                    .setIcon(data.icon)
                    .setOnMenuItemClickListener(item -> {
                        if (data.callback != null) {
                            data.callback.run();
                            return true;
                        }
                        return false;
                    });
        }
    }

    private List<Data> getDefaultDataSource() {
        List<Data> dataList = new ArrayList<>();
        dataList.add(new Data(R.string.add_new_friend, R.drawable.ic_menu_friend));
        dataList.add(new Data(R.string.scan_qr_code, R.drawable.ic_menu_qrcode));
        dataList.add(new Data(R.string.receive_payment, R.drawable.ic_menu_card));
        dataList.add(new Data(R.string.help_and_feedback, R.drawable.ic_menu_feedback));
        return dataList;
    }


    public class Data {
        public int title;
        public int icon;
        public Runnable callback;

        public Data(int title, int icon) {
            this(title, icon, null);
        }

        public Data(int title, int icon, Runnable callback) {
            this.title = title;
            this.icon = icon;
            this.callback = callback;
        }
    }

}
