package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WithDrawListActivity extends AppCompatActivity {
    private String openid;
    private List<OrderBean> mList = new ArrayList<>();

    private Context mContext;
    private Map<Integer,Boolean> isChecked = new HashMap<Integer, Boolean>();

    private double now_num = 0.0, all_num = 0.0;
    private int now_choose = 0;


    private TextView tv_totalAccount;
    private CheckBox allChek;

    private Button btn_withDraw;
    private WithDrawListAdapter mWithDrawListAdapter;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_with_draw_list);
        Intent i = getIntent();
        openid = i.getStringExtra("openid");
        initView();
    }

    private void initView() {
        listView = findViewById(R.id.WitdrawOrderList);
        allChek = findViewById(R.id.all_check);
        btn_withDraw = findViewById(R.id.withDraw_btn);
        allChek.setChecked(true);
        tv_totalAccount = findViewById(R.id.total_amount);
        OkHttpClient client = new OkHttpClient();
        mContext = getApplicationContext();
        RequestBody formbody = new FormBody.Builder()
                .add("Openid",openid)
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.GetUnDraw)
                .post(formbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("ANYI","请求失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String content = response.body().string();
                //List<OrderBean> orderBeans = new ArrayList<>();
                //Log.d(TAG,content);
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String orderArrayStr = jsonObject.getString("err_msg");
                    JSONArray jsonArray = new JSONArray(orderArrayStr);
                    //Log.d(TAG, String.valueOf(jsonArray.length()));
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        OrderBean tempOrder = gson.fromJson(jsonArray.get(i).toString(),OrderBean.class);
                        mList.add(tempOrder);
                        isChecked.put(i,true);
                        now_num += Double.parseDouble(tempOrder.getAccount());
                        all_num += Double.parseDouble(tempOrder.getAccount());
                        //Log.d(TAG,"第" + i + "个订单" + tempOrder.toString());
                    }
//                    Toast.makeText(mContext,"共" + mList.size() + "个订单", Toast.LENGTH_LONG).show();
                    //Log.d(TAG,"子线程2" + Thread.currentThread().getName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWithDrawListAdapter = new WithDrawListAdapter(mList,isChecked,mContext);
                            listView.setAdapter(mWithDrawListAdapter);
                            DecimalFormat df = new DecimalFormat("0.00");
                            now_num /= 100;
                            String formattedNumber = df.format(now_num);
                            tv_totalAccount.setText("合计：  " + formattedNumber);
                            now_choose++;

                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.d("ANYI","LIST SIZE" + mList.size());
                //Log.d(TAG,"state + " + states[Currentstate]);
            }
        });
        allChek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allChek.isChecked() == false) {
                    for (int i = 0; i < mList.size(); ++i) {
                        isChecked.put(i,false);
                        now_choose = 0;
                        now_num = 0.00;
                    }
                }
                else if (allChek.isChecked() == true) {
                    for (int i = 0; i < mList.size(); ++i) {
                        isChecked.put(i,true);
                        now_choose = mList.size();
                        now_num = all_num;
                    }
                }
                mWithDrawListAdapter.notifyDataSetChanged();
                DecimalFormat df = new DecimalFormat("0.00");
                now_num /= 100;
                String formattedNumber = df.format(now_num);
                tv_totalAccount.setText("合计：  " + formattedNumber);

            }
        });
        btn_withDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orders = "";
                String ids = "";
                int count = 0;
                int total_money = 0;
                for (int i = 0; i < mList.size(); i++) {
                    if (isChecked.get(i) == true) {
                        OrderBean tempOrder = mList.get(i);
                        orders += count==0?tempOrder.getOrder_id():"," + tempOrder.getOrder_id();
                        ids += count==0?tempOrder.getId():"," + tempOrder.getId();
                        count++;
                        total_money += Integer.parseInt(tempOrder.getAccount());
                    }
                }
                if (count == 0){
                    Toast.makeText(mContext,"请选择要提现的订单",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("ANYI","orders: " + orders);
                Log.d("ANYI","IDS: " + ids);
                Log.d("ANYI", "Count: " + String.valueOf(count));
                Log.d("ANYI","ACCOUNT :" + String.valueOf(total_money));
                RequestBody formbody = new FormBody.Builder()
                        .add("Openid",openid)
                        .add("Arr_Codes", orders)
                        .add("Arr_Ids",ids)
                        .add("All_Count", String.valueOf(count))
                        .add("All_Account", String.valueOf(total_money))
                        .build();
                Request request = new Request.Builder()
                        .url(API_Adaress.Draw_order)
                        .post(formbody)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("ANYI","请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String content = response.body().string();
                        //List<OrderBean> orderBeans = new ArrayList<>();
                        //Log.d(TAG,content);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(content);
                            int err_code = jsonObject.getInt("err_code");
                            String err_msg = jsonObject.getString("err_msg");
                            Log.d("ANYI",err_msg);
                            if (err_code == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"提现成功",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("result","返回结果");
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"提现失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
            }
        });
    }
    public class WithDrawListAdapter extends BaseAdapter{
        private List<OrderBean> list;
        private Map<Integer, Boolean> isCheck = new HashMap<>();

        private Context mContext;

        public WithDrawListAdapter(List<OrderBean> list,  Map<Integer, Boolean> isCheck, Context context) {
            this.list = list;
            this.isCheck = isCheck;
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return list.size();
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
            OrderBean temp = list.get(i);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.withdrawitem, null);
            CheckBox checkBox = view.findViewById(R.id.ischeked);
            checkBox.setChecked(isCheck.get(i));
            TextView des = view.findViewById(R.id.order_start_to_end);
            TextView price = view.findViewById(R.id.order_price);
            TextView time = view.findViewById(R.id.order_time);
            String description = "[" + temp.getStart_place() + "-" + temp.getDest_place() + "]";
            DecimalFormat df = new DecimalFormat("0.00");
            float pri = (float) (Float.parseFloat(temp.getAccount()) / 100.0);
            String formattedNumber = df.format(pri);
            description = description + formattedNumber + "元。";
            des.setText(description);
            price.setText(formattedNumber);
            time.setText(temp.getFinish_time());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.d("ANYI", "NOW CHOOSE" + now_choose);
                    if (checkBox.isChecked() == false) {
                        isCheck.put(i,false);
                        now_num -= pri;
                        String formattedNumber1 = df.format(now_num);
                        tv_totalAccount.setText("合计：  " + formattedNumber1);
                        now_choose--;
                        if (now_choose < list.size())allChek.setChecked(false);
                    }
                    else if (checkBox.isChecked() == true) {
                        isCheck.put(i,true);
                        now_num += pri;
                        String formattedNumber1 = df.format(now_num);
                        tv_totalAccount.setText("合计：  " + formattedNumber1);
                        now_choose++;
                        if (now_choose == list.size())allChek.setChecked(true);
                    }
                }
            });
            return view;
        }
    }

}