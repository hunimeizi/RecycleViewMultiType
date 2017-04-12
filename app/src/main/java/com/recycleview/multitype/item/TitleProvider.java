package com.recycleview.multitype.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.recycleview.multitype.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Duo Nuo on 2017/4/12.
 */
public class TitleProvider extends ItemViewProvider<Title, TitleProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_title, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Title title) {
        holder.tv_sectionDivide_left.setText(title.title);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sectionDivide_left;

        ViewHolder(View itemView) {
            super(itemView);
            tv_sectionDivide_left = (TextView) itemView.findViewById(R.id.tv_sectionDivide_left);
        }
    }
}
