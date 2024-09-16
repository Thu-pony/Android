package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BankAccountDetailAct extends AppCompatActivity {
    private static final String TAG = "ADDBANKCARD";
    private EditText et_cardnum, et_cardbank, et_holdername, et_phonenum, et_verfity;
    private Button btn_getverfify, btn_bind;
    private OkHttpClient mHttpClient;
    private String openid;
    private Context mContext;
    private final int sc_code = 0;
    private SharedPreferences prefs;
    private final int mErr_code = -1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_detail);
        prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = prefs.getString("openid", null);
        mContext = this;
        mHttpClient = new OkHttpClient();
        initview();
    }

    private void initview() {
        et_cardnum = findViewById(R.id.editTextbankaccount);
        et_cardbank = findViewById(R.id.editTextbankname);
        et_holdername = findViewById(R.id.editTextusername);
        et_phonenum = findViewById(R.id.editTextPhone);
        et_verfity = findViewById(R.id.editTextverifycode);
        btn_getverfify = findViewById(R.id.btn_getVeriryCode);
        btn_bind = findViewById(R.id.btn_bind);
        btn_getverfify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phonenum.getText().toString();
                Log.d(TAG, phone);
                RequestBody formbody = new FormBody.Builder()
                        .add("Token", openid)
                        .add("Mobile", phone)
                        .add("Type", "2")
                        .build();
                Request request = new Request.Builder()
                        .url(API_Adaress.getverifycode)
                        .post(formbody)
                        .build();

                mHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG,"连接失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String content = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int err_code = jsonObject.getInt("err_code");
                            if (err_code == sc_code) runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"已发送",Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phonenum.getText().toString();
                String bank_name = et_cardbank.getText().toString();
                String card_num = et_cardnum.getText().toString();
                String holder = et_holdername.getText().toString();
                String verfiy_code = et_verfity.getText().toString();
                RequestBody formbody = new FormBody.Builder()
                        .add("Token", openid)
                        .add("Phone", phone)
                        .add("BankName", bank_name)
                        .add("BankNum",card_num)
                        .add("CardName", holder)
                        .add("Veri_code",verfiy_code)
                        .build();
                Request request = new Request.Builder()
                        .url(API_Adaress.bind_card)
                        .post(formbody)
                        .build();
                mHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG,"连接失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String content = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int err_code = jsonObject.getInt("err_code");
                            if (err_code == mErr_code)runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext,"验证码错误",Toast.LENGTH_LONG).show();
                                }
                            });
                            else if(err_code == sc_code) {
                                String card_info = bank_name + "#" + card_num + ",";
                                SharedPreferences.Editor editor = prefs.edit();
                                String ord_card_info = prefs.getString("card_info", "");
                                ord_card_info += card_info;
                                editor.putString("card_info",ord_card_info);
                                editor.apply();
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"绑定失败",Toast.LENGTH_LONG).show();
                                    }
                                });
                                setResult(Activity.RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });
            }
        });
    }
}