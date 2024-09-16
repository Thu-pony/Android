package csumissu.fakewechat.v2.ui.main;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import csumissu.fakewechat.R;
import csumissu.fakewechat.comments.CommentsActivity;
import csumissu.fakewechat.v2.adapter.TileGapDelegate.TileGap;
import csumissu.fakewechat.v2.adapter.TileMenuDelegate.TileMenu;
import csumissu.fakewechat.v2.base.AbsSimpleFragment;


/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class ExploreFragment extends AbsSimpleFragment {

    @Override
    protected List<Object> loadData() {
        List<Object> list = new ArrayList<>();
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_comments)
                .setNameRes(R.string.comments)
                .setIntent(new Intent(getActivity(), CommentsActivity.class))
                .create());
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_scan)
                .setNameRes(R.string.scan_qr_code)
                .create());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_shake)
                .setNameRes(R.string.shake)
                .create());
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_nearby)
                .setNameRes(R.string.nearby)
                .create());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_bottle)
                .setNameRes(R.string.bottle)
                .create());
        list.add(new TileGap());
        list.add(new TileMenu.Builder()
                .setIconRes(R.drawable.ic_game)
                .setNameRes(R.string.game)
                .create());
        return list;
    }
}
