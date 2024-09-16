package csumissu.fakewechat.v2.ui.main;

import java.util.ArrayList;
import java.util.List;

import csumissu.fakewechat.R;
import csumissu.fakewechat.v2.adapter.TileGroupDelegate.TileGroup;
import csumissu.fakewechat.v2.base.AbsSimpleFragment;

/**
 * @author sunyaxi
 * @date 2016/11/19.
 */
public class HomeFragment extends AbsSimpleFragment {

    @Override
    protected List<Object> loadData() {
        List<Object> list = new ArrayList<>();
        list.add(new TileGroup(R.drawable.ic_robot, R.string.robot));
        return list;
    }
}
