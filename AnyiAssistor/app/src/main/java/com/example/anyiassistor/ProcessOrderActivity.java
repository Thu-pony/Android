package com.example.anyiassistor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProcessOrderActivity extends AppCompatActivity {
    private RelativeLayout mRelativeLayout;
    private TextView tv_order_date_text;
    private TextView tv_order_startplace_text;
    private TextView tv_order_endplace_text;
    private TextView tv_order_fee_text;
    private OrderBean mOrderBean;
    private Button btn_Return;
    private Context mContext;
    private Button btn_bind;
    private Button btn_start;
    private Button btn_finish;
    private String mID;
    private String openid;
    private final String TAG = "processorder";

    private int bindFail = -1004;
    private int bindSec = 0;
    private int startSec = 0;
    private OkHttpClient mHttpClient;
    private String mState = "6";
    private DeviceBean mDevice = null;
    private SharedPreferences msharedPreferences;
    private MapView mapView;
    private TencentMap mTenecentMap;
    private String devices_code = "";
    private WebsockteManger websockteManger = WebsockteManger.getInstance(this);


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        IntentFilter filter = new IntentFilter(WebsockteManger.ACTION_WEBSOCKET_MESSAGE_Device);
        registerReceiver(DeviceInfoReceiver,filter);
        TencentMapInitializer.setAgreePrivacy(true);
        mapView = findViewById(R.id.device_map);
        setContentView(R.layout.activity_process_order);
        Intent intent = getIntent();
        mOrderBean = intent.getParcelableExtra("MyOrderBean");
        msharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        openid = msharedPreferences.getString("openid", null);
        devices_code = msharedPreferences.getString("device_code","300200200016");
        mID = mOrderBean.getId();
        mState = mOrderBean.getState();
        Log.d(TAG,openid);
        Log.d(TAG,mID);
        mHttpClient = new OkHttpClient();
        btn_bind = (Button) findViewById(R.id.bind_btn);
        btn_start = (Button) findViewById(R.id.start_btn);
        btn_finish = (Button) findViewById(R.id.finish_btn);
        String time = mOrderBean.getAppoint_end_time();
        Log.d("TAG", "time" + time);
        boolean res = false;
        try {
            res = compareDate(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (res == true && mState.equals("2")) btn_bind.setVisibility(View.VISIBLE);
        else if (mState.equals("3")) btn_start.setVisibility(View.VISIBLE);
        else if (mState.equals(("6")))btn_finish.setVisibility(View.VISIBLE);
        initView();
    }

    public boolean compareDate(String time1) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date a = sdf.parse(time1);
        Date now = new Date();
        //Date类的一个方法，如果a早于b返回true，否则返回false
        return now.before(a);
    }

    private void initView() {
        showmap();
        mRelativeLayout = (RelativeLayout) findViewById(R.id.order_info);
        tv_order_date_text = (TextView) mRelativeLayout.findViewById(R.id.order_date_text);
        tv_order_startplace_text = (TextView) mRelativeLayout.findViewById(R.id.order_startplace_text);
        tv_order_endplace_text = (TextView) mRelativeLayout.findViewById(R.id.order_endplace_text);
        tv_order_fee_text = (TextView) mRelativeLayout.findViewById(R.id.order_fee_text);
        tv_order_date_text.setText(mOrderBean.getAppoint_date_txt() + " " + mOrderBean.getStart_time() + "_" + mOrderBean.getEnd_time());
        tv_order_date_text.setTextSize(16);
        tv_order_startplace_text.setText(mOrderBean.getStart_place());
        tv_order_endplace_text.setText(mOrderBean.getDest_place());
        tv_order_fee_text.setText(mOrderBean.getTotal_fee_txt());
        btn_Return = (Button) mRelativeLayout.findViewById(R.id.return_btn);
        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ANYI", "点击了返回的按钮");
                setResult(1);
                finish();
            }
        });
        btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("温馨提示");
                builder.setMessage("您确定要绑定设备");
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
                        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(mContext);
                        scanner.startScan().addOnSuccessListener(new OnSuccessListener<Barcode>() {
                            @Override
                            public void onSuccess(Barcode barcode) {
                                Log.d(TAG,barcode.getRawValue());
                                String url = barcode.getRawValue();
                                String scan_Code = url.substring(url.lastIndexOf('/') + 1, url.indexOf('='));
                                String v_Code = url.substring(url.lastIndexOf('=') + 1);
                                Log.d(TAG, "v_code = " + v_Code);
                                Log.d(TAG, "scan_code = " + scan_Code);
                                RequestBody formbody = new FormBody.Builder()
                                        .add("Token",openid)
                                        .add("Scan_code",scan_Code)
                                        .add("V_code",v_Code)
                                        .add("Order_id",mID)
                                        .build();
                                Request request = new Request.Builder()
                                        .url(API_Adaress.BindDevice)
                                        .post(formbody)
                                        .build();
                                mHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                        Log.d(TAG,"访问失败");
                                    }

                                    @Override
                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                        String content = response.body().string();
                                        Log.d(TAG,content);
                                        JSONObject jsonObject = null;
                                        try {
                                            jsonObject = new JSONObject(content);
                                            String respnseRes = jsonObject.getString("err_msg");
                                            int responseCode = jsonObject.getInt("err_code");
                                            if (responseCode == bindFail) {
                                                dialog.dismiss();
                                                Log.d(TAG,"绑定失败");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast toast = Toast.makeText(mContext, "绑定失败", Toast.LENGTH_LONG);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }
                                                });
                                            } else if (responseCode == bindSec) {
                                                Log.d(TAG,"绑定成功");
                                                SharedPreferences.Editor editor = msharedPreferences.edit();
                                                editor.putString("device_code",scan_Code);
                                                editor.commit();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        btn_bind.setVisibility(View.GONE);
                                                        btn_start.setVisibility(View.VISIBLE);
                                                    }
                                                });

                                            }
                                        } catch (JSONException e) {


                                        }

                                    }
                                });
                            }
                        });
                    }
                        // 点击“是”后的操作
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody formbody = new FormBody.Builder()
                        .add("Token",openid)
                        .add("OrderId",mOrderBean.getId())
                        .build();
                Request request = new Request.Builder()
                        .url(API_Adaress.StartOrder)
                        .post(formbody)
                        .build();
                mHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG,"开始失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String content = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int ResponseCode = jsonObject.getInt("err_code");
                            if (ResponseCode == startSec) {
                               // String device_code = msharedPreferences.getString("device_code","300200200016");
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("cmd","A1");
                                jsonObject1.put("token",openid);
                                jsonObject1.put("codes",devices_code);
                                websockteManger.sendMessage(jsonObject1.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"订单已开始",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"开始订单失败",Toast.LENGTH_LONG).show();
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
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody formbody = new FormBody.Builder()
                        .add("Token",openid)
                        .add("OrderId",mOrderBean.getId())
                        .build();
                Request request = new Request.Builder()
                        .url(API_Adaress.FinishOrder)
                        .post(formbody)
                        .build();
                mHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d(TAG,"开始失败");
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String content = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            int ResponseCode = jsonObject.getInt("err_code");
                            if (ResponseCode == startSec) {
                                String device_code = msharedPreferences.getString("device_code","");
                                JSONObject jsonObject1 = new JSONObject();
                                jsonObject1.put("cmd","A1");
                                jsonObject1.put("token",openid);
                                jsonObject1.put("codes",device_code);
                                websockteManger.sendMessage(jsonObject1.toString());
                                SharedPreferences.Editor editor = msharedPreferences.edit();
                                editor.putString("device_code","");
                                editor.commit();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"订单已结束",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"结束订单失败",Toast.LENGTH_LONG).show();
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
    private BroadcastReceiver DeviceInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"接收到广播");
            mDevice = intent.getParcelableExtra("device");
            mTenecentMap = mapView.getMap();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker1_activated3x);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 4, originalBitmap.getHeight() / 4, false);
            // 设置地图的初始位置
            Double Lat = mDevice.getLat();
            Double Lng =  mDevice.getLng();
            LatLng location = new LatLng(Lat, Lng);
            mTenecentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 150, 0, 0)));
            Log.d(TAG,"Lat = " + Lat);
            Log.d(TAG, "Lng = " + Lng);
            mTenecentMap.addMarker(new MarkerOptions(location).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
        }
    };
    private void showmap() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd","B");
            jsonObject.put("token",openid);
            jsonObject.put("codes",devices_code);
            websockteManger.sendMessage(jsonObject.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onDestroy() {
        if (DeviceInfoReceiver != null)
            unregisterReceiver(DeviceInfoReceiver);
        super.onDestroy();
    }
}

