package com.example.anyiassistor;


import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public interface IAdapterDelegate {

    int getViewType();

    boolean isForViewType(List items, int position);

    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(List items, int position, RecyclerView.ViewHolder holder);
}
