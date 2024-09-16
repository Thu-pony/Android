package csumissu.fakewechat.main;

import android.content.Intent;
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
import csumissu.fakewechat.R;
import csumissu.fakewechat.comments.CommentsActivity;
import csumissu.fakewechat.common.TileAdapter;
import csumissu.fakewechat.common.TileGapDelegate;
import csumissu.fakewechat.common.TileMenuDelegate;

/**
 * @author sunyaxi
 * @date 2016/6/27
 */
public class ExploreFragment extends Fragment {

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
        List<Object> items = new ArrayList<>();
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_comments, R.string.comments,
                new Intent(getActivity(), CommentsActivity.class)));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_scan, R.string.scan_qr_code));
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_shake, R.string.shake));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_nearby, R.string.nearby));
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_bottle, R.string.bottle));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.ic_game, R.string.game));
        adapter.setData(items);
    }

}
