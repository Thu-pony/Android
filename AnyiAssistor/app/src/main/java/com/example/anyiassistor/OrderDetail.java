package com.example.anyiassistor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderDetail extends AppCompatActivity {
    private TextView tv_fee, tv_start_place, tv_end_place, tv_rev_time, tv_contact, tv_contact_man, tv_order_id, tv_apponit_time, tv_charge, tv_finishtime;

    private Context mContext;

    private OrderBean mOrderBean;

    private ConstraintLayout mConstraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mContext = this;
        Intent intent = getIntent();
        mOrderBean = intent.getParcelableExtra("MyOrderBean");
        initView();
    }

    private void initView() {
        tv_fee = findViewById(R.id.text_amount);
        mConstraintlayout = findViewById(R.id.order_detail);
        tv_start_place = findViewById(R.id.text_order_startPlace);
        //Log.d("ANYI", String.valueOf(tv_start_place == null));
        tv_end_place = findViewById(R.id.text_order_endPlace);
        tv_rev_time = findViewById(R.id.text_order_Appoint_Time);
        tv_contact = findViewById(R.id.text_order_Contact);
        tv_contact_man = findViewById(R.id.text_order_Contact_man);
        tv_order_id = findViewById(R.id.text_order_id);
        tv_apponit_time = findViewById(R.id.text_order_Appoint_Time_txt);
        tv_charge = findViewById(R.id.text_order_charge);
        tv_finishtime = findViewById(R.id.text_order_endTime);
        tv_fee.setText("￥" + mOrderBean.getTotal_fee_txt());
        SpannableString spannableString = new SpannableString("出发地  " + mOrderBean.getStart_place());
        ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.GRAY);
        // 设置第五到第七个字符的颜色为红色
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(graySpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_start_place.setText(spannableString);
        spannableString = new SpannableString("目的地  " + mOrderBean.getDest_place());
        spannableString.setSpan(graySpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_end_place.setText(spannableString);
        String date = mOrderBean.getAppoint_date_txt();
        spannableString = new SpannableString("预约时间  " + date.substring(3,5) + "月" + date.substring(6,date.length()) + "日" + " " + mOrderBean.getStart_time() + "-" + mOrderBean.getEnd_time());
        spannableString.setSpan(graySpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_rev_time.setText(spannableString);
        spannableString = new SpannableString("联系人  " + mOrderBean.getContact_man());
        spannableString.setSpan(graySpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_contact.setText(spannableString);
        spannableString = new SpannableString("联系方式  " + mOrderBean.getContact());
        spannableString.setSpan(graySpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_contact_man.setText(spannableString);
        spannableString = new SpannableString("订单号  " + mOrderBean.getOrder_id());
        spannableString.setSpan(graySpan, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_order_id.setText(spannableString);
        spannableString = new SpannableString("下单时间  " + mOrderBean.getAppoint_start_time());
        spannableString.setSpan(graySpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_apponit_time.setText(spannableString);
        spannableString = new SpannableString("服务人员  " + mOrderBean.getContact_man());
        spannableString.setSpan(graySpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_charge.setText(spannableString);
        spannableString = new SpannableString("完成时间  " + mOrderBean.getAppoint_end_time());
        spannableString.setSpan(graySpan, 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(blackSpan, 6, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_finishtime.setText(spannableString);
    }
}