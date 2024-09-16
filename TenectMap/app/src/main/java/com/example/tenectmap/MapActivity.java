package com.example.tenectmap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private TencentMap tencentMap;
    private RelativeLayout relativeLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        relativeLayout = findViewById(R.id.parents);
        TencentMapInitializer.setAgreePrivacy(true);
        mapView =  find ViewById(R.id.mapview);
        //mapView.onCreate(savedInstanceState);
        //mapView = new MapView(this);
        tencentMap = mapView.getMap();

        // 设置地图的初始位置
        LatLng location = new LatLng(29.445394, 119.906743); // 上海市的经纬度
        tencentMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(location, 20, 0, 0)));
        LatLng location2 = new LatLng(29.445404, 119.906759); // 上海市的经纬度
        // 添加标记
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker1_activated3x);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.getWidth() / 3, originalBitmap.getHeight() / 3, false);
        //BitmapDescriptor custom = BitmapDescriptorFactory.fromResource(R.drawable.marker1_activated3x);
        tencentMap.addMarker(new MarkerOptions(location).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
        tencentMap.addMarker(new MarkerOptions(location2).icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)));
        //relativeLayout.addView(mapView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }
}
