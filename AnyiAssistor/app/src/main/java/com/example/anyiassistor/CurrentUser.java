package com.example.anyiassistor;

import android.content.Context;
import android.content.SharedPreferences;

public class CurrentUser {
    private static CurrentUser instance;
    private Context context; // 应用的上下文

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String email;
    private String password;

    private CurrentUser(Context context) {
        this.context = context.getApplicationContext();
        loadUserInfo();
    }

    public static synchronized CurrentUser getInstance(Context context) {
        if (instance == null) {
            instance = new CurrentUser(context);
        }
        return instance;
    }

    // 从SharedPreferences加载用户信息
    private void loadUserInfo() {
        SharedPreferences prefs = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        username = prefs.getString("username", null);
        password = prefs.getString("password", null);
    }

    // 将用户信息保存到SharedPreferences
    public void saveUserInfo() {
        SharedPreferences prefs = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
    //   editor.putString("email", email);
        editor.putString("password", password);
        editor.commit();
    }
}
