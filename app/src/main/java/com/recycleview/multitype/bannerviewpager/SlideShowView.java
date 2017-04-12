package com.recycleview.multitype.bannerviewpager;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.recycleview.multitype.R;
import com.recycleview.multitype.transformer.AccordionTransformer;
import com.recycleview.multitype.view.NetImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: LYBo
 * Create at: 2016-02-16 10:53
 * Class:
 */
public class SlideShowView extends FrameLayout {

    /**
     * 图片轮播视图
     */
    private LoopViewPager mAdvPager = null;
    /**
     * 滚动图片视图适配
     */
    private ImageCycleAdapter mAdvAdapter;
    private AdImageCycleAdapter adImageCycleAdapter;

    /**
     * 图片轮播指示器控件
     */
    private PageIndicaorView pageIndicaor;

    private ArrayList<String> imageNameList;

    private boolean isStop = false;

    /**
     * @param context
     */
    public SlideShowView(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
        setListener();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_ad_cycle, this, true);
        mAdvPager = (LoopViewPager) findViewById(R.id.adv_pager);

        mAdvPager.setBoundaryCaching(true);
        mAdvPager.setPageTransformer(true, new AccordionTransformer());

        initViewPagerScroll();

        // 滚动图片右下指示器视
        pageIndicaor = (PageIndicaorView) findViewById(R.id.adv_pager_indicaor);
    }

    /**
     * 设置ViewPager的滑动速度
     */
    private void initViewPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(
                    mAdvPager.getContext());
//            scroller.setScrollDuration(1500);
            mScroller.set(mAdvPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        mAdvPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
    }

    public void setImageUris(Context context, List<String> imageUrlList, ImageCycleViewListener imageCycleViewListener) {
        setImageUris(context, imageUrlList, null, imageCycleViewListener);
    }

    /**
     * 装填图片数据
     */
    public void setImageUris(Context context, List<String> imageUrlList, ArrayList<String> imageNameList, ImageCycleViewListener imageCycleViewListener) {
        if (null == imageUrlList) {
            imageUrlList = new ArrayList<>();
        }

        stopImageTimerTask();

        this.imageNameList = imageNameList;
//        mAdvAdapter = new ImageCycleAdapter(getContext(), imageUrlList, imageCycleViewListener);
//        mAdvPager.setAdapter(mAdvAdapter);

        adImageCycleAdapter = new AdImageCycleAdapter(context);
        adImageCycleAdapter.setListSource(imageUrlList);
        adImageCycleAdapter.setImageCycleViewListener(imageCycleViewListener);
        mAdvPager.setAdapter(adImageCycleAdapter);
        pageIndicaor.setViewPager(mAdvPager);

        startImageTimerTask();
    }

    /**
     * 页面进入前台时启动自动循环
     */
    public void onResume() {
        startImageTimerTask();
    }

    /**
     * 页面进入后台时关闭自动滚动
     */
    public void onPause() {
        stopImageTimerTask();
    }

    /**
     * 图片滚动任务
     */
    private void startImageTimerTask() {
        if (null == mAdvPager || null == mAdvPager.getAdapter()) {
            return;
        }

        if (adImageCycleAdapter != null && adImageCycleAdapter.getCount() < 2) {
            return;
        }

        isStop = false;
        // 图片滚动
        mHandler.removeCallbacks(mImageTimerTask);
        mHandler.postDelayed(mImageTimerTask, 1500);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        isStop = true;
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {
        @Override
        public void run() {
            if (null == mAdvPager || null == mAdvPager.getAdapter()) {
                return;
            }

            int currentItem = mAdvPager.getCurrentItem();

            mAdvPager.setCurrentItem(++currentItem, true);
            if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
                mHandler.postDelayed(mImageTimerTask, 1500);
            }
        }
    };

    private class AdImageCycleAdapter extends RecyclingPagerAdapter {

        private List<String> listSource;

        private ViewHolder holder;
        private Context context;

        AdImageCycleAdapter(Context context) {
            this.context = context;
        }

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        public void setListSource(List<String> listSource) {
            this.listSource = listSource;
        }

        public void setImageCycleViewListener(ImageCycleViewListener mImageCycleViewListener) {
            this.mImageCycleViewListener = mImageCycleViewListener;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup container) {
            if (null == convertView) {
                holder = new ViewHolder();
                holder.imageView = new NetImageView(getContext());
                holder.imageView.setIsNeedRound(false);
                holder.imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.imageView.setDefaultImage(R.mipmap.default_banner);
                convertView = holder.imageView;
            }
            holder.imageView.setImageUrl(context, getItem(position), holder.imageView);

            // 设置图片点击监听
            holder.imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageCycleViewListener != null) {
                        mImageCycleViewListener.onImageClick(position, v);
                    }
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return (null == listSource) ? 0 : listSource.size();
        }

        public String getItem(int position) {
            return (null == listSource || position >= listSource.size()) ? null : listSource.get(position);
        }

        class ViewHolder {
            private NetImageView imageView;
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片资源列表
         */
        private List<String> mAdList = new ArrayList<>();

        /**
         * 广告图片点击监听
         */
        private ImageCycleViewListener mImageCycleViewListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, List<String> adList, ImageCycleViewListener imageCycleViewListener) {
            this.mContext = context;
            this.mAdList = adList;
            this.mImageCycleViewListener = imageCycleViewListener;
        }

        @Override
        public int getCount() {
            return null == mAdList ? 0 : mAdList.size();
        }

        public String getItem(int position) {
            return (null == mAdList || position >= mAdList.size()) ? "" : mAdList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = getItem(position);
            NetImageView imageView = new NetImageView(mContext);
            imageView.setIsNeedRound(false);
            imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setDefaultImage(R.mipmap.default_banner);
            imageView.setImageUrl(mContext, imageUrl, imageView);

            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mImageCycleViewListener != null) {
                        mImageCycleViewListener.onImageClick(position, v);
                    }
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public interface ImageCycleViewListener {
        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, View imageView);
    }

}
