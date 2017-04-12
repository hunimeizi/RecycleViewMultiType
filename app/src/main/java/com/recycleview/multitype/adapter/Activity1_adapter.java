package com.recycleview.multitype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recycleview.multitype.R;

import java.util.List;

/**
 * Created by Duo Nuo on 2017/4/12.
 */

public class Activity1_adapter extends RecyclerView.Adapter<Activity1_adapter.myViewHolder> {

    private Context mContext;
    private List<String> list;

    public Activity1_adapter(Context context, List<String> datas) {
        this.mContext = context;
        this.list = datas;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_activity1_recycle, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.tv_recycle_item.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recycle_item;

        myViewHolder(View itemView) {
            super(itemView);
            tv_recycle_item = (TextView) itemView.findViewById(R.id.tv_recycle_item);
        }
    }
}
