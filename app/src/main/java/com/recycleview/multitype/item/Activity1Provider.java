package com.recycleview.multitype.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recycleview.multitype.R;
import com.recycleview.multitype.adapter.Activity1_adapter;
import com.recycleview.multitype.multitype.ItemViewProvider;


/**
 * Created by Duo Nuo on 2017/4/12.
 */
public class Activity1Provider extends ItemViewProvider<Activity1, Activity1Provider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_activity1, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Activity1 activity1) {

        Activity1_adapter adapter = new Activity1_adapter(holder.itemView.getContext(), activity1.getActivity_items());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.itemView.getContext(), 4);
        holder.recycle_activity1.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        holder.recycle_activity1.setAdapter(adapter);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recycle_activity1;

        ViewHolder(View itemView) {
            super(itemView);
            recycle_activity1 = (RecyclerView) itemView.findViewById(R.id.recycle_activity1);
        }
    }
}
