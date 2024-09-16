package com.example.anyiassistor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    public void init() {
        sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("openid", null) == null) {
            Toast.makeText(this, "请先进行登录！", Toast.LENGTH_SHORT).show();
            final Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
            Timer timer=new Timer();
            TimerTask timerTask=new TimerTask() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            };
            timer.schedule(timerTask,1500);
        }else {
            try {
                Timer timer=new Timer();
                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        //String _id = sharedPreferences.getString("id", null);
                        String openid = sharedPreferences.getString("openid",null);

                        //String password = sharedPreferences.getString("username", null);
                        //String password1 = sharedPreferences.getString("userpassword", null);
                        try {
                            //开始登录，这段自行发挥

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        final Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                };
                timer.schedule(timerTask,1500);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
