package com.example.anyiassistor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PendingFragment extends Fragment {
    private View view;
    private ListView mListView;

    private List<OrderBean> mList = new ArrayList<>();
    private CurrentUser user;
    private Context mContext;
    private HashMap<String, List<OrderBean>> mMap;
    private ActivityResultLauncher<Intent> PendingActivityResultLauncher;
    private PendingOrderApapter mPendingOrderApapter;
    public PendingFragment(){};
    public PendingFragment(List<OrderBean> List) {
        mList = List;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pending,null);
       // Log.d("aaaaaa","调用了");
        mContext = getContext();
        initView();
        registerReceiver();
        return view;
    }

    private void initView() {
        mListView = view.findViewById(R.id.pendding_listview);
        Log.d("PedingFragment size", "等待中的订单" + mList.size());
        PendingActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 0) {

                }
            }
        });
        mPendingOrderApapter = new PendingOrderApapter(getContext(),mList);
        mListView.setAdapter(mPendingOrderApapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderBean tempOredeBean = mList.get(i);
                Log.d("ANYI",tempOredeBean.getOrder_id());
                Intent intent = new Intent(getContext(), AcceptOrderActivity.class);
                intent.putExtra("MyOrderBean", tempOredeBean);
                PendingActivityResultLauncher.launch(intent);
            }
        });
    }
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("WebSocketManager", "接收到广播");
            if (WebsockteManger.ACTION_WEBSOCKET_MESSAGE.equals(intent.getAction())) {
                OrderBean orderBean = intent.getParcelableExtra("new_order");
                Log.d("WebSocketManager", "接收到数据 " + orderBean.toString());
                // 处理接收到的数据
                // 头部插入
                mList.add(0,orderBean);
                mPendingOrderApapter.notifyDataSetChanged();
            }
        }
    };
    private void registerReceiver() {
        if (updateReceiver != null) {
            Log.d("WebSocketManager", "广播注册");
            IntentFilter filter = new IntentFilter(WebsockteManger.ACTION_WEBSOCKET_MESSAGE);
            getActivity().registerReceiver(updateReceiver, filter);
        }
    }

    private void unregisterReceiver() {
        if (updateReceiver != null) {
            Log.d("WebSocketService", "广播销毁");
            getActivity().unregisterReceiver(updateReceiver);
            updateReceiver = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    private class PendingOrderApapter extends BaseAdapter {
        private List<OrderBean> mOrderList;
        private Context mContext;
        public PendingOrderApapter(Context context, List<OrderBean> mList) {
            mContext = context;
            mOrderList = mList;
        }
        @Override
        public int getCount() {
            return mOrderList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewHolder viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.orderitem, null);
            TextView date = (TextView) view.findViewById(R.id.order_date_text);
            TextView start_palce =  (TextView) view.findViewById(R.id.order_startplace_text);
            TextView dest_palce =  (TextView) view.findViewById(R.id.order_endplace_text);
            TextView total_fee =  (TextView) view.findViewById(R.id.order_fee_text);
            OrderBean temp = mOrderList.get(i);
            date.setText(temp.getAppoint_date_txt() + " " + temp.getStart_time() + "-" + temp.getEnd_time());
            start_palce.setText(temp.getStart_place());
            dest_palce.setText(temp.getDest_place());
            total_fee.setText(temp.getTotal_fee_txt());
            //info.setText(mOrderList.get(i).getOrder_info());
            //viewHolder.setImage_view(order_pic);
            //viewHolder.setInfo_view(info);
            //viewHolder.setTitle_view(title);
            return  view;
        }
        class ViewHolder {
            public TextView getTitle_view() {
                return title_view;
            }

            public void setTitle_view(TextView title_view) {
                this.title_view = title_view;
            }

            public ImageView getImage_view() {
                return image_view;
            }

            public void setImage_view(ImageView image_view) {
                this.image_view = image_view;
            }

            public TextView getInfo_view() {
                return info_view;
            }

            public void setInfo_view(TextView info_view) {
                this.info_view = info_view;
            }

            private TextView title_view;
            private ImageView image_view;
            private TextView info_view;
        }
    }

}
