package com.example.anyiassistor;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityBankAccount extends AppCompatActivity {
    private ImageButton addBankaccount;
    private Context mContext;
    private RecyclerView recyclerView;
    private BankAdapter bankAdapter;
    private ActivityResultLauncher<Intent> ActivityResultLauncher;
    private SharedPreferences pref;
    private List<Carditem> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main_bank_account);
        pref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String card_info = pref.getString("card_info", null);
        ActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            pref = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                            String card_info = pref.getString("card_info", null);
                            initData(card_info);
                            bankAdapter.notifyDataSetChanged();
                        }
                    }
                });
        initData(card_info);
        initview();
    }

    public void initview() {
        addBankaccount = findViewById(R.id.add_bankcount);
        recyclerView = findViewById(R.id.card_list);
        bankAdapter = new BankAdapter(mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bankAdapter);
        addBankaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(ActivityBankAccount.this)
                        .setTitle("温馨提示")
                        .setMessage("收款账号需要实名认证，否则会导致收款失败")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(mContext, BankAccountDetailAct.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ActivityResultLauncher.launch(intent);
                            }
                        })
                        .create();
                dialog.show();
            }
        });
    }

    public void initData(String card_info) {
        if (card_info != null) {
            String[] cardArray = card_info.split(",");
            for (String s : cardArray) {
                Carditem carditem = new Carditem();
                String[] signle_card_info = s.split("#");
                carditem.setBank_name(signle_card_info[0]);
                carditem.setCard_num(signle_card_info[1]);
                mList.add(carditem);
            }
        }
    }
}