package com.example.anyiassistor;

import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderFragment extends Fragment {
    private View view;
    private ListView mListView;

    private List<OrderBean> mList = new ArrayList<>();
  //  private HashMap<String, List<OrderBean>> mMap;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.orderindex_layout,null);
        initView();
        return view;
    }
    public OrderFragment(){};
    public OrderFragment(List<OrderBean> list) {
        mList = list;
        // mMap = map;
    }
    private void initView() {
        mListView = view.findViewById(R.id.all_order_listview);
        Log.d("homeFragment", "已经完成的订单" + mList.size());
        //mList = mMap.get("已完成");
        /*for (int i = 0; i < 20; i++) {
            String title = "进行中 订单号： " + "B000" + i;
            int src = R.drawable.item1;
            String info = "预计结束" + i + "天后" ;
            OrderBean tempOrderBean = new OrderBean(title,src,info);
            mList.add(tempOrderBean);
        }*/
        AllOrderApapter allOrderApapter = new AllOrderApapter(getContext(),mList);
        mListView.setAdapter(allOrderApapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrderBean tempOredeBean = mList.get(i);
       //         OrderBean myOrderBean  = new OrderBean (tempOredeBean.getOrder_id(),tempOredeBean.getOrder_title(),tempOredeBean.getOreder_src(),tempOredeBean.getOrder_info());
                 Intent intent = new Intent(getContext(), OrderDetail.class);
                 intent.putExtra("MyOrderBean", tempOredeBean);
               startActivity(intent);
            }
        });
    }

    private class AllOrderApapter extends BaseAdapter {
        private List<OrderBean> mOrderList;
        private Context mContext;
        public AllOrderApapter(Context context, List<OrderBean> mList) {
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
            view = inflater.inflate(R.layout.orderitem, null);
            ViewHolder viewHolder = new ViewHolder();
            TextView title = (TextView) view.findViewById(R.id.order_title);
            ImageView order_pic = (ImageView) view.findViewById(R.id.order_picture);
            TextView info = (TextView) view.findViewById(R.id.order_info);
          //  title.setText(mOrderList.get(i).getOrder_title());
            //order_pic.setImageResource(mOrderList.get(i).getOreder_src());
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

