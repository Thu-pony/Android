package com.example.anyiassistor;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


public class TileAdapter extends RecyclerView.Adapter {

    public static final int TYPE_GAP = 1;
    public static final int TYPE_MENU = 2;
    public static final int TYPE_INFO = 3;
    private Context mContext;
    private TileGapDelegate mGapDelegate;
    private TileMenuDelegate mMenuDelegate;
    private TileInfoDelegate mInfoDelegate;
    private MineFragment mineFragment;
    private List mItems = new ArrayList<>();

    private OnItemClickListener listener;

    public TileAdapter(MineFragment mineFragment,Context context) {
        this.mineFragment = mineFragment;
        mContext = context;
        mGapDelegate = new TileGapDelegate(context);
        mMenuDelegate = new TileMenuDelegate(context);
        mInfoDelegate = new TileInfoDelegate(context);
    }

    public void setData(List items) {
        mItems.clear();
        mItems.addAll(items);
    }
    public void setOnItemClickLister(OnItemClickListener clickLister) {
        this.listener = clickLister;
    }

    @Override
    public int getItemViewType(int position) {
        if (mGapDelegate.isForViewType(mItems, position)) {
            return TYPE_GAP;
        } else if (mMenuDelegate.isForViewType(mItems, position)) {
            return TYPE_MENU;
        } else if (mInfoDelegate.isForViewType(mItems, position)) {
            return TYPE_INFO;
        }
        throw new IllegalArgumentException("No delegate found for position " + position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_GAP:
               return mGapDelegate.onCreateViewHolder(parent);
            case TYPE_MENU:
                return mMenuDelegate.onCreateViewHolder(parent);
            case TYPE_INFO:
                return mInfoDelegate.onCreateViewHolder(parent);
            default:
                throw new IllegalArgumentException("No delegate found for position " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_GAP:
                mGapDelegate.onBindViewHolder(mItems, position, holder);
                break;
            case TYPE_MENU:
                mMenuDelegate.onBindViewHolder(mItems, position, holder);
                break;
            case TYPE_INFO:
                mInfoDelegate.onBindViewHolder(mItems, position, holder);
                break;
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (listener != null) {
                   switch (holder.getAdapterPosition()) {
                       case 3:
                           ViewPager viewPager = mineFragment.getActivity().findViewById(R.id.viewpager);
                           OrderFragment orderFragment = (OrderFragment) viewPager.getAdapter().instantiateItem(viewPager,1);
                           viewPager.setCurrentItem(1);
                           break;

                       case 5:
                           i = new Intent(mContext, ActivityWithdrawal.class);
                           mContext.startActivity(i);
                           break;
                       case 7:
                           i = new Intent(mContext, ActivityBankAccount.class);
                           mContext.startActivity(i);
                           break;
                       case 9:
                           if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                               ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
                           }
                           i = new Intent(Intent.ACTION_CALL);
                           i.setData(Uri.parse("tel:13905795667"));
                           mContext.startActivity(i);
                           break;
                       case 13:
                           SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_info",Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           editor.putString("username",null);
                           editor.putString("password",null);
                           i = new Intent(mContext, LoginActivity.class);
                           mContext.startActivity(i);
                           editor.commit();
                       default:
                           break;
                   }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
