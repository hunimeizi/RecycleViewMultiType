package com.recycleview.multitype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recycleview.multitype.R;
import com.recycleview.multitype.item.Activity_item;
import com.recycleview.multitype.view.NetImageView;

import java.util.List;

/**
 * Created by Duo Nuo on 2017/4/12.
 */

public class Activity1_adapter extends RecyclerView.Adapter<Activity1_adapter.myViewHolder> {

    private Context mContext;
    private List<Activity_item> list;

    public Activity1_adapter(Context context, List<Activity_item> datas) {
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
        holder.tv_recycle_item.setText(list.get(0).getTitle_string().get(position));
        holder.image_src.setImageUrl(holder.itemView.getContext(),list.get(0).getUrl_string().get(position),holder.image_src);
    }

    @Override
    public int getItemCount() {
        return list.get(0).getTitle_string().size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recycle_item;
        NetImageView image_src;
        myViewHolder(View itemView) {
            super(itemView);
            tv_recycle_item = (TextView) itemView.findViewById(R.id.tv_recycle_item);
            image_src= (NetImageView) itemView.findViewById(R.id.image_src);
        }
    }
}
