package com.recycleview.multitype.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.recycleview.multitype.R;
import com.recycleview.multitype.bannerviewpager.SlideShowView;
import com.recycleview.multitype.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Duo Nuo on 2017/4/12.
 */
public class BannerProvider extends ItemViewProvider<Banner, BannerProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Banner banner) {
        assertGetAdapterNonNull();
        List<String> adList = new ArrayList<>();
        adList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491999407289&di=8d533e51533b2736f8d40273b26b01e4&imgtype=0&src=http%3A%2F%2Fpic50.nipic.com%2Ffile%2F20141014%2F8442159_182826708000_2.jpg");
        adList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491999407289&di=5f10e8abfbd795f918fc1f7dd7e8cb33&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F14%2F27%2F45%2F71r58PICmDM_1024.jpg");
        adList.add("hhttps://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491999407288&di=28db0a666178ca4a6576f6b000c84113&imgtype=0&src=http%3A%2F%2Fpic35.nipic.com%2F20131121%2F3822951_143839966000_2.jpg");
        holder.slideShowView_banner.setImageUris(holder.itemView.getContext(), adList, imageCycleViewListener);
    }
    private SlideShowView.ImageCycleViewListener imageCycleViewListener = new SlideShowView.ImageCycleViewListener() {
        @Override
        public void onImageClick(int position, View imageView) {
//            轮播图片item点击事件
        }
    };
    private void assertGetAdapterNonNull() {
        if (getAdapter() == null) {
            throw new NullPointerException("getAdapter() == null");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        SlideShowView slideShowView_banner;
        ViewHolder(View itemView) {
            super(itemView);
            slideShowView_banner = (SlideShowView) itemView.findViewById(R.id.slideShowView_banner);
            ViewGroup.LayoutParams lp = slideShowView_banner.getLayoutParams();
            lp.width = DisplayUtils.getScreenWidth(itemView.getContext());
            lp.height = DisplayUtils.getScreenHeight(itemView.getContext()) / 4;
            slideShowView_banner.setLayoutParams(lp);
        }
    }
}
