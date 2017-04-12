package com.recycleview.multitype.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recycleview.multitype.R;


/**
 * 自动以ImageView 整合ImageLoader
 */
public class NetImageView extends ImageView {

    /**
     * 圆角
     */
    private boolean isNeedRound = false;

    /**
     * 默认图片ID
     */
    private int defaultImageResId;

    public NetImageView(Context context) {
        super(context);
    }

    public NetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NetImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setIsNeedRound(boolean isNeedRound) {
        this.isNeedRound = isNeedRound;
    }

    public void setDefaultImage(int defaultImageResId) {
        this.defaultImageResId = defaultImageResId;
    }

    public void setImageUrl(Context context, String url, NetImageView netImageView) {
        if (TextUtils.isEmpty(url)) {
            if (defaultImageResId != 0) {
                setImageResource(defaultImageResId);
            }
            return;
        }
        Glide.with(context)
                .load(url).placeholder(R.mipmap.product_default).error(R.mipmap.product_default)
                .centerCrop()
                .crossFade()
                .into(netImageView);
    }

}
