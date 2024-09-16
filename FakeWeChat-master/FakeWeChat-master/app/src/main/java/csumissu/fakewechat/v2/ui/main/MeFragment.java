package csumissu.fakewechat.v2.ui.main;

import java.util.ArrayList;
import java.util.List;

import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.AppProps;
import csumissu.fakewechat.v2.adapter.TileGapDelegate.TileGap;
import csumissu.fakewechat.v2.adapter.TileMenuDelegate.TileMenu;
import csumissu.fakewechat.v2.adapter.TileUserDelegate.TileUser;
import csumissu.fakewechat.v2.base.AbsSimpleFragment;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class MeFragment extends AbsSimpleFragment {

    @Override
    protected List<Object> loadData() {
        List<Object> list = new ArrayList<>();
        list.add(new TileGap());
        list.add(new TileUser(AppProps.getOwner()));
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_panorama)
                .setNameRes(R.string.album)
                .create());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_star)
                .setNameRes(R.string.star)
                .create());
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_insert_emoticon)
                .setNameRes(R.string.emotion)
                .create());
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_settings)
                .setNameRes(R.string.settings)
                .create());
        return list;
    }

}
