package com.example.anyiassistor;


import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;



public class MainActivity extends AppCompatActivity {
    private final String TAG = "MAINACTIVITY";
    // viewpager
    private ViewPager mViewPager;
    private List<View> mViews;

    private List<Fragment> mFragments = new ArrayList<>();
    private HashMap<String, List<OrderBean>> mMap = new HashMap<>();

    private List<OrderBean> mPendingList = new ArrayList<>();
    private List<OrderBean> mProcessList = new ArrayList<>();

    private List<OrderBean> mFinishList = new ArrayList<>();
    private HashMap<String, List<OrderBean>> map = new HashMap<String, List<OrderBean>>();
    private String openid;
    //RadioGroup
    private RadioGroup mRadioGroup;
    //3个tabz
    private RadioButton mHome, mOrder, mMine;
    private Context mContext;
    //1表示待处理，2 进行中，3完成中
    private String[] states = {"1","2","3"};
    private int CurrentState = 0;
    private OkHttpClient mHttpClient;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //registerReceiver(updateReceiver,null,0);
        //IntentFilter filter = new IntentFilter(WebsockteManger.ACTION_WEBSOCKET_MESSAGE);
        //LocalBroadcastManager.getInstance(this).registerReceiver(updateReceiver, filter);
        mContext = this;
        mMap.put(states[0],mPendingList);
        mMap.put(states[1],mProcessList);
        mMap.put(states[2],mFinishList);
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = sharedPreferences.getString("openid",null);
        Log.d(TAG,"OPENID = " + openid);
        if (openid == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        mHttpClient = new OkHttpClient();
        //new LoadDataTask().execute();
        loadData(openid,0,mMap.get(states[0]));
        //loadData(openid,"2",mProcessList);
        //loadData(openid,"3",mFinishList);
        //initview();
        Log.d(TAG,"主线程" + Thread.currentThread().getName());
        Intent serviceIntent = new Intent(this, WebSocketService.class);
        startService(serviceIntent);
    }
    private void loadData(String openid, int  Currentstate, List<OrderBean> mList) {
        RequestBody formbody = new FormBody.Builder()
                .add("Token",openid)
                .add("State",states[Currentstate])
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.GetOrders)
                .post(formbody)
                .build();
        Log.d(TAG,"子线程1" + Thread.currentThread().getName());
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //e.printStackTrace();
                Log.d(TAG,e.toString());
                Log.d(TAG,"请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String content = response.body().string();
                List<OrderBean> orderBeans = new ArrayList<>();
                Log.d(TAG,content);
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String orderArrayStr = jsonObject.getString("err_msg");
                    JSONArray jsonArray = new JSONArray(orderArrayStr);
                    Log.d(TAG, String.valueOf(jsonArray.length()));
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        OrderBean tempOrder = gson.fromJson(jsonArray.get(i).toString(),OrderBean.class);
                        mList.add(tempOrder);
                       // Log.d(TAG,"第" + i + "个订单" + tempOrder.toString());
                    }
//                    Toast.makeText(mContext,"共" + mList.size() + "个订单", Toast.LENGTH_LONG).show();
                    Log.d(TAG,"子线程2" + Thread.currentThread().getName());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                    Log.d(TAG,"LIST SIZE" + mList.size());
                    Log.d(TAG,"state + " + states[Currentstate]);
                    /*if (Currentstate == states.length) {
                        initview();
                    } else if (Currentstate < states.length) {
                        Currentstate++;
                        loadData(openid, Currentstate, mList);
                    }*/
                  loadNext();
                //response.close();

            }
        });

    }

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"接收到广播");
            if (WebsockteManger.ACTION_WEBSOCKET_MESSAGE.equals(intent.getAction())) {
                OrderBean orderBean = intent.getParcelableExtra("new_order");
                Log.d(TAG, "接收到数据 " + orderBean.toString());
                // 处理接收到的数据
                // 头部插入
                mPendingList.add(0,orderBean);
            }
        }
    };
    private void loadNext() {
        CurrentState++;
        Log.d(TAG,"LoadNext的线程 " + Thread.currentThread().getName());
        if (CurrentState < states.length) loadData(openid,CurrentState,mMap.get(states[CurrentState]));
        else if (CurrentState == states.length) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initview();
                }
            });
        }
    }


    private void initview() {
        //初始化控件
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mRadioGroup = findViewById(R.id.rg_tab);
        mHome = findViewById(R.id.home_button);
        mOrder = findViewById(R.id.order_button);
        mMine = findViewById(R.id.mine_button);
        mHome.setChecked(true);
        HomeFragment homeFragment = new HomeFragment(mPendingList,mProcessList);
        OrderFragment orderFragment = new OrderFragment(mFinishList);
        MineFragment mineFragment = new MineFragment();
        mFragments.add(homeFragment);
        mFragments.add(orderFragment);
        mFragments.add(mineFragment);
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mFragments);

        /*mViews = new ArrayList<View>(); // 加载视图
        //添加3个view进来
        mViews.add(LayoutInflater.from(this).inflate(R.layout.homeindex_layout,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.orderindex_layout,null));
        mViews.add(LayoutInflater.from(this).inflate(R.layout.mineindex_layout,null));*/
        //给viewpager设置一个适配器
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Log.d(TAG,"点击了首页按钮");
                        mHome.setChecked(true);
                        mOrder.setChecked(false);
                        mMine.setChecked(false);
                        break;
                    case 1:
                        Log.d(TAG,"点击了订单按钮");
                        mHome.setChecked(false);
                        mOrder.setChecked(true);
                        mMine.setChecked(false);
                        break;
                    case 2:
                        Log.d(TAG,"点击了我的按钮");
                        mHome.setChecked(false);
                        mOrder.setChecked(false);
                        mMine.setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //监听是否被选中
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d(TAG, "i == " + String.valueOf(i));
                if (i == R.id.home_button) {
                    mViewPager.setCurrentItem(0);
                }
                else if (i == R.id.order_button) {
                    mViewPager.setCurrentItem(1);
                }
                else
                    mViewPager.setCurrentItem(2);
            }
        });

    }
    private class MyvewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mViews.get(position));
            return mViews.get(position);
        }
    }
    private class LoadDataTask extends AsyncTask<Void, Void, HashMap<String,List<OrderBean>>> {
        @Override
        protected HashMap<String,List<OrderBean>> doInBackground(Void... voids) {
//            loadPending(openid,"1",mPendingList);
 //           loadPending(openid,"2",mProcessList);
   //         loadPending(openid,"3",mFinishList);

            mMap.put("待处理",mPendingList);
            mMap.put("进行中",mProcessList);
            mMap.put("已经完成",mProcessList);
            return mMap;
        }

        @Override
        protected void onPostExecute(HashMap<String,List<OrderBean>> data) {
            // 数据加载完毕，更新ListView
            //PendingFragment.PendingOrderApapter pendingOrderApapter = new PendingFragment.PendingOrderApapter(getContext(),mList);
            //mListView.setAdapter(pendingOrderApapter);
           // initview();
            //Log.d(TAG,"Pending size == " + mPendingList.size());
            //Log.d(TAG, "Process size == " + mProcessList.size());
            //Log.d(TAG,"Finish size == " + mFinishList.size());
            //try {
              //  Thread.sleep(5000);
            //} catch (InterruptedException e) {
              //  throw new RuntimeException(e);
            //}


        }
    }
}