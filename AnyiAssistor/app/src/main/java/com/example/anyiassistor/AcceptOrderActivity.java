package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AcceptOrderActivity extends AppCompatActivity {
    private RelativeLayout mRelativeLayout;
    private TextView tv_order_date_text;
    private TextView tv_order_startplace_text;
    private TextView tv_order_endplace_text;
    private TextView tv_order_fee_text;
    private OrderBean mOrderBean;
    private Button btn_Accept, btn_Return;
    private Context mContext;

    private String openid;
    private final  String TAG = "ACCEPT_ORDER";
    private String mID;
    private int accptFail = -1004;
    private int accptSucess = 0;
    private MapView mapView;
    private TencentMap mTenecentMap;
    private OkHttpClient mHttpClient;
    private List<DeviceBean> mDevices = new ArrayList<>();
    private final int sec = 0;
    private double Lat = 0.0;
    private double Lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_order);
        mapView = findViewById(R.id.devices_map);
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        TencentMapInitializer.setAgreePrivacy(true);
        openid = sharedPreferences.getString("openid",null);
        Log.d(TAG,"OPENID = " + openid);
        mOrderBean = intent.getParcelableExtra("MyOrderBean");
        mID = mOrderBean.getId();
        Log.d(TAG,"mID " + mID);
        mHttpClient = new OkHttpClient();
        //Log.d("ANYI", tempOrderbean.getOrder_id());
        mContext = this;
        initView();
    }

    private void initView() {
        showmap();
        mRelativeLayout = (RelativeLayout)findViewById(R.id.order_info);
        tv_order_date_text = (TextView) mRelativeLayout.findViewById(R.id.order_date_text);
        tv_order_startplace_text = (TextView) mRelativeLayout.findViewById(R.id.order_startplace_text);
        tv_order_endplace_text =  (TextView) mRelativeLayout.findViewById(R.id.order_endplace_text);
        tv_order_fee_text = (TextView) mRelativeLayout.findViewById(R.id.order_fee_text);
        tv_order_date_text.setText(mOrderBean.getAppoint_date_txt() + " " + mOrderBean.getStart_time() + "_" + mOrderBean.getEnd_time());
        tv_order_date_text.setTextSize(16);
        tv_order_startplace_text.setText(mOrderBean.getStart_place());
        tv_order_endplace_text.setText(mOrderBean.getDest_place());
        tv_order_fee_text.setText(mOrderBean.getTotal_fee_txt());
        btn_Accept = (Button) mRelativeLayout.findViewById(R.id.accept_order);
        btn_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("ANYI", "点击了受理订单的按钮");
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("温馨提示");
                builder.setMessage("您确定要受理该订单？");


// 添加否定按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“否”后的操作
                        dialog.dismiss();
                    }
                });
                // 添加肯定按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“是”后的操作
                        RequestBody formbody = new FormBody.Builder()
                                .add("Token",openid)
                                .add("OrderId",mID)
                                .build();
                        Request request = new Request.Builder()
                                .url(API_Adaress.AceptOrders)
                                .post(formbody)
                                .build();
                        mHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.d("TAG","请求失败");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String content = response.body().string();
                                try {
                                    JSONObject jsonObject = new JSONObject(content);
                                    String respnseRes = jsonObject.getString("err_msg");
                                    int responseCode = jsonObject.getInt("err_code");
                                    if (responseCode == accptFail) {
                                        dialog.dismiss();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast toast = Toast.makeText(mContext,"受理失败",Toast.LENGTH_LONG);
                                                toast.setGravity(Gravity.CENTER,0,0);
                                                toast.show();
                                            }
                                        });
                                    } else if (responseCode == 0) {
                                        dialog.dismiss();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                                builder.setTitle("受理成功");
                                                builder.setMessage("请您约定时间到指定地点");
                                                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                });

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

// 创建并显示AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();
                //setResult(0);
              //  finish();
            }
        });
        btn_Return = (Button) mRelativeLayout.findViewById(R.id.return_btn);
        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ANYI", "点击了返回的按钮");
                setResult(1);
                finish();
            }
        });
    }

    private void showmap(){
        RequestBody formbody = new FormBody.Builder()
                .add("Token",openid)
                .build();
        Request request = new Request.Builder()
                .url(API_Adaress.Devices_list)
                .post(formbody)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG","请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String content = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    int err_code = jsonObject.getInt("err_code");
                    if (err_code == sec) {
                        String devices = jsonObject.getString("err_msg");
                        JSONArray device_Array = new JSONArray(devices);
                        Gson gson = new Gson();
                        for (int i = 0; i < device_Array.length(); i++) {
                            DeviceBean devicesBean = new DeviceBean();
                            devicesBean.setId(device_Array.getJSONObject(i).getInt("id"));
                            devicesBean.setLat(device_Array.getJSONObject(i).getDouble("latitude"));
                            devicesBean.setLng(device_Array.getJSONObject(i).getDouble("longitude"));
                            Lat += devicesBean.getLat();
                            Lng += devicesBean.getLng();
                            mDevices.add(devicesBean);
                        }
                        Lat /= device_Array.length();
                        Lng /= device_Array.length();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTenecentMap = mapView.getMap();
                                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker1_activated3x);
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 4, originalBitmap.getHeight() / 4, false);
                                // 设置地图的初始位置
                                LatLng location = new LatLng(Lat, Lng);
                                mTenecentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 150, 0, 0)));
                                //Log.d(TAG,"Lat = " + Lat);
                                //Log.d(TAG, "Lng = " + Lng);
                                for (int i = 0; i < mDevices.size(); i++) {
                                    DeviceBean devicesBean = mDevices.get(i);
                                    mTenecentMap.addMarker(new MarkerOptions(new LatLng(devicesBean.getLat(),devicesBean.getLng())).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}