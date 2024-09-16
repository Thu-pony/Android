package com.example.anyiassistor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityWithdrawal extends AppCompatActivity {
    private String openid;
    private TextView tv_accountNum, tv_detailwithdrawl;
    private Button btn_withdram;

    private Context mContext;
    private ActivityResultLauncher<Intent> withDrawActivityResultLauncher, withDrawlDetailLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        mContext = getApplicationContext();
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = sharedPreferences.getString("openid",null);
        withDrawActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d("ANYI","提现成功");
                }
            }
        });
        withDrawlDetailLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 0) {

                }
            }
        });
        initView();
    }

    private void initView() {
        tv_detailwithdrawl = findViewById(R.id.withdrawL_detail);
        tv_accountNum = findViewById(R.id.account_num);
        btn_withdram = findViewById(R.id.btn_withdram);
        OkHttpClient client = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder()
                .add("Openid",openid)
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.Getbanace)
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
                String body = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    int err_code = jsonObject.getInt("err_code");
                    if (err_code == 0) {
                        Double err_msg = jsonObject.getDouble("err_msg");
                        double account = err_msg / 100.0;
                        DecimalFormat format = new DecimalFormat("0.00");
                        String str = format.format(account);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_accountNum.setText("￥" + str);
                            }
                        });
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        btn_withdram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WithDrawListActivity.class);
                intent.putExtra("openid",openid);
                withDrawActivityResultLauncher.launch(intent);
            }
        });
        tv_detailwithdrawl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityDetailWithDrwal.class);
                intent.putExtra("openid",openid);
                withDrawlDetailLauncher.launch(intent);
            }
        });
    }
}