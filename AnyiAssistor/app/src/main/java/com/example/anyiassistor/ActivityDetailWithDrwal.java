package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityDetailWithDrwal extends AppCompatActivity {
    private TextView tv_now;
    private Context mContext;
    private Dialog dialog = null;
    private List<WithDrawOrder> mList = new ArrayList<>();

    private List<WithDrawOrder> mUsedList = new ArrayList<>();
    private OkHttpClient mOkHttpClient;

    private SharedPreferences mShare;
    private String openid;

    private TextView tv_empty;

    private ListView listViewData;

    private SimpleDateFormat mDateFormat;

    private  int mYear, mMonth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mOkHttpClient = new OkHttpClient();
        mShare = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = mShare.getString("openid",null);
        setContentView(R.layout.activity_detail_with_drwal);
        tv_now = findViewById(R.id.now_date);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
        tv_now.setText(mYear + "年" + mMonth + "月");
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        initData();
    }

    private void initData() {
        RequestBody formbody = new FormBody.Builder()
                .add("Openid",openid)
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.Draw_list)
                .post(formbody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.d("TAG","请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String content = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(content);
                    String orderArrayStr = jsonObject.getString("err_msg");
                    JSONArray jsonArray = new JSONArray(orderArrayStr);
                    //Log.d(TAG, String.valueOf(jsonArray.length()));
                    Gson gson = new Gson();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        WithDrawOrder tempOrder = gson.fromJson(jsonArray.get(i).toString(), WithDrawOrder.class);
                        JSONObject tempJson = new JSONObject(jsonArray.get(i).toString());
                        tempOrder.setAbstract(tempJson.getString("abstract"));
                        mList.add(tempOrder);
                        Date date = mDateFormat.parse(tempOrder.getCreated_at());
                        int year = date.getYear() + 1900;
                        int month = date.getMonth() + 1;
                        if (year == mYear && month == mMonth)mUsedList.add(tempOrder);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                        // Log.d(TAG,"第" + i + "个订单" + tempOrder.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void initView() {

        tv_empty = findViewById(R.id.textViewEmpty);
        listViewData = findViewById(R.id.listViewData);
        DetailListAdapter detailListAdapter = new DetailListAdapter(mContext, mUsedList);
        listViewData.setAdapter(detailListAdapter);
        if (mUsedList.size() == 0) {
            tv_empty.setVisibility(View.VISIBLE);
            listViewData.setVisibility(View.GONE);
        }else {
            tv_empty.setVisibility(View.GONE);
            listViewData.setVisibility(View.VISIBLE);
        }
        tv_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.date_year_month_view);
                }
                Button btn_left = (Button) dialog.findViewById(R.id.btn_left);
                Button btn_right = (Button) dialog.findViewById(R.id.btn_right);
                final MonthPicker monthPicker = (MonthPicker) dialog.findViewById(R.id.month_picker);
                //tv_body_msg.setText(R.string.dialog_msg);
                dialog.setCancelable(true);
                //点击左侧按钮
                btn_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //点击右键
                btn_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int chose_year = monthPicker.getYear();
                        int chose_month = monthPicker.getMonth() + 1;
                        mUsedList.clear();
                        for (int i = 0; i < mList.size(); i++) {
                            Date date = null;
                            try {
                                date = mDateFormat.parse(mList.get(i).getCreated_at());
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            int year = date.getYear() + 1900;
                            int month = date.getMonth() + 1;
                            if (year == chose_year && month == chose_month)mUsedList.add(mList.get(i));
                        }
                        if (mUsedList.size() == 0) {
                            tv_empty.setVisibility(View.VISIBLE);
                            listViewData.setVisibility(View.GONE);
                        }else {
                            tv_empty.setVisibility(View.GONE);
                            listViewData.setVisibility(View.VISIBLE);
                            detailListAdapter.notifyDataSetChanged();
                        }
                        tv_now.setText(chose_year + "年" + chose_month + "月");
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }
    public class DetailListAdapter extends ArrayAdapter<WithDrawOrder> {
        public DetailListAdapter(Context context, List<WithDrawOrder> transactions) {
            super(context, 0, transactions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WithDrawOrder transaction = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.detailitem, parent, false);
            }
            TextView textViewDescription = convertView.findViewById(R.id.textViewDescription);
            TextView textViewTimestamp = convertView.findViewById(R.id.textViewTimestamp);
            TextView textViewAmount = convertView.findViewById(R.id.textViewAmount);

            textViewDescription.setText("银行卡" + transaction.getAbstract());
            textViewTimestamp.setText(transaction.getCreated_at());
            String money = transaction.getAbstract().substring(2,transaction.getAbstract().length() - 1);
            textViewAmount.setText(money);
            if (transaction.getState() == 2) {
                textViewAmount.setTextColor(Color.RED);
            } else {
                textViewAmount.setTextColor(Color.BLACK);
            }

            return convertView;
        }
    }

}