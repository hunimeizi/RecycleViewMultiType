package com.recycleview.multitype.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recycleview.multitype.R;


public class DefaultLoadMoreView extends RelativeLayout implements ILoadMoreView {

    private TextView mTvMessage;
    private ProgressBar mPbLoading;
    private LinearLayout lin_load_more;
    private Context context;

    public DefaultLoadMoreView(Context context) {
        super(context);
        init(context);
        this.context = context;
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        this.context = context;
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        this.context = context;
    }

    private void init(Context context) {
        inflate(context, R.layout.isw_loading_view_footer_default, this);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mTvMessage = (TextView) findViewById(R.id.tv_loading_msg);
        lin_load_more = (LinearLayout) findViewById(R.id.lin_load_more);

    }

    @Override
    public void showNormal() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.isw_search_more_load);
    }

    @Override
    public void showNoMore() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.isw_loading_view_no_me);
    }

    public String getTvMessage() {
        return (String) mTvMessage.getText();
    }

    @Override
    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.isw_loading_view_loading);

    }

    @Override
    public void showFail() {
        Log.d("lyl", "--------showFail: ");
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(R.string.isw_loading_view_net_error);
    }

    @Override
    public View getFooterView() {
        return this;
    }

    @Override
    public void goneLoading() {
        mPbLoading.setVisibility(View.GONE);
        mTvMessage.setVisibility(View.GONE);
    }

    @Override
    public void VisibleMoreBtn() {
        lin_load_more.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneMoreBtn() {
        lin_load_more.setVisibility(View.GONE);
    }

    @Override
    public void onClickLoadMoreView() {
        lin_load_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void setViewTag(int id,String tag) {
        lin_load_more.setTag(id, tag);
    }
}
