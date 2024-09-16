package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText userPassword = null;
    private EditText userName = null;
    private Button btn_login = null;
    private CheckBox agreeCheckBox = null;

    private Context mContext;
    private Button btn_register = null;
    private String openid;
    private String phone;
    private String role;
    private String company;
    private static String TAG = "登录活动";
    @SuppressLint("HandlerLeak")
    private final Handler hand = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
               Toast toast =  Toast.makeText(mContext, "登录失败", Toast.LENGTH_LONG);
               toast.setGravity(Gravity.CENTER,0,0);
               toast.show();
            } else if (msg.what == 1) {
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("openid",openid);
                editor.putString("phone", phone);
                editor.putString("role", role);
                editor.putString("company",company);
                editor.putString("device_code","");
                editor.commit();
                Intent i = new Intent(mContext, MainActivity.class);
                startActivity(i);
            } else if (msg.what == 2) {
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a", "注册");
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", userName.getText().toString());
                editor.putString("password", userPassword.getText().toString());
                editor.commit();
                startActivity(intent);
                // setResult(RESULT_CANCELED,intent);
                //  finish();
            } else if (msg.what == 3) {
                Toast.makeText(getApplicationContext(), "该账号不存在", Toast.LENGTH_LONG).show();
            } else if (msg.what == 4) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //将想要传递的数据用putExtra封装在intent中
                intent.putExtra("a", "登录");
                SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", userName.getText().toString());
                editor.putString("password", userPassword.getText().toString());
                editor.commit();
                startActivity(intent);
            } else if (msg.what == 5) {
                Toast.makeText(getApplicationContext(), "密码错误登录失败", Toast.LENGTH_LONG).show();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_login);
        userName = (EditText) findViewById(R.id.input_phone);
        userPassword = (EditText) findViewById(R.id.input_password);
        btn_login = (Button) findViewById(R.id.login);
        agreeCheckBox = (CheckBox) findViewById(R.id.agreeCheckBox);
        String text = "我已阅读并同意《安易服务协议》";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK),0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),7,text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreeCheckBox.setText(spannableString);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString().trim();
                String usepwd = userPassword.getText().toString().trim();
                if (username == null || usepwd == null) {
                    Toast toast = Toast.makeText(LoginActivity.this, "请先输入手机号和密码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }else if (agreeCheckBox.isChecked() == false) {
                    Toast toast = Toast.makeText(LoginActivity.this, "登录前请阅读服务协议", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    return;
                }
                else {
                    sendLoginRequest(username, usepwd);
                }
               /* User user = new User();
                user.setName(username);
                user.setPassWord(usepwd);
                //普通用户为0， 管理员为1
                user.setUserTYpe(0);
                new Thread() {
                    @Override
                    public void run() {
                        int msg = 5;
                        UserDao userDao = new UserDao();
                        User uu = userDao.findUser(user.getName());
                        if (uu == null) {
                            msg = 3;
                        } else {
                             msg = userDao.login(user.getName(), user.getPassWord());
                        }
                        hand.sendEmptyMessage(msg);
                    }
                }.start();*/

            }
        });
    }
    private void sendLoginRequest(String username, String userpassword) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder()
                .add("UserName",username)
                .add("Pass",userpassword)
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.Login)
                .post(formbody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG,"请求失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //Log.d(TAG,response.body().string());
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String errcode = jsonObject.getString("errcode");
                    Log.d(TAG,"登录状态 " + errcode);
                    if (errcode.equals("-1002")) {
                        int msg = 0;
                        hand.sendEmptyMessage(msg);
                    } else if (errcode.equals("0000")) {
                        String errmsg = jsonObject.getString("errmsg");
                        JSONObject jsonObject1 = new JSONObject(errmsg);
                        User u = new User();
                        openid = jsonObject1.getString("openid");
                        phone = jsonObject1.getString("nickname");
                        role = jsonObject1.getString("role");
                        company = jsonObject1.getString("company");
                        u.setOpenid(openid);
                        u.setPhone(phone);
                        u.setRole(role);
                        u.setCompany(company);
                        hand.sendEmptyMessage(1);
                        
                    }
                    String errmsg = jsonObject.getString("errmsg");
                    Log.d(TAG, "登录信息" + errmsg);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}