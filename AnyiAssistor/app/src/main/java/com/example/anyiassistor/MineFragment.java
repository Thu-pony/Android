package com.example.anyiassistor;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerview;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mineindex_layout,null);
        initview();
        return  view;
    }
    public MineFragment(){};

    private void initview() {
        mRecyclerview = view.findViewById(R.id.mine_recycler_view);
        mRecyclerview.setMotionEventSplittingEnabled(false);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        TileAdapter adapter = new TileAdapter(this,getContext());
        setDefaultData(adapter);
        adapter.setOnItemClickLister(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("AAAAA","Fragment 点击了" + position);
            }
        });
        mRecyclerview.setAdapter(adapter);
    }

    private void setDefaultData(TileAdapter adapter) {
        String versioname = "";
        PackageManager packageManager = getContext().getPackageManager();
        String packageName = getContext().getPackageName();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName,0);
            versioname = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        List items = new ArrayList<>();
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileInfoDelegate.TileInfo());
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.icon_order_selected, R.string.my_order));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.icon_input, R.string.my_input));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.bank_card, R.string.my_account));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.icon_rule, R.string.contact_severice));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.icon_xieyi, R.string.vesion));
        items.add(new TileGapDelegate.TileGap());
        items.add(new TileMenuDelegate.TileMenu(R.drawable.exit_icon, R.string.exit));
        items.add(new TileGapDelegate.TileGap());
        adapter.setData(items);
    }
}
