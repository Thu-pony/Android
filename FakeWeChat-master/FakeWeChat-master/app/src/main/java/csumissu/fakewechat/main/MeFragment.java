package csumissu.fakewechat.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import csumissu.fakewechat.AppContext;
import csumissu.fakewechat.R;
import csumissu.fakewechat.common.TileAdapter;
import csumissu.fakewechat.common.TileGapDelegate;
import csumissu.fakewechat.common.TileInfoDelegate;
import csumissu.fakewechat.common.TileMenuDelegate;

/**
 * @author sunyaxi
 * @date 2016/6/27
 */
public class MeFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        // 禁止多点触发
        mRecyclerView.setMotionEventSplittingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
                .colorResId(R.color.whiteSmoke)
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.tile_divider_margin)
                .build());
        TileAdapter adapter = new TileAdapter(getContext());
        setDefaultData(adapter);
        mRecyclerView.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void setDefaultData(TileAdapter adapter) {
        List items = new ArrayList<>();
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileInfoDelegate.TileInfo(AppContext.getInstance().getLoginUser()));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_panorama, R.string.album));
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_star, R.string.star));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_insert_emoticon, R.string.emotion));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_settings, R.string.settings));
        adapter.setData(items);
    }
}
