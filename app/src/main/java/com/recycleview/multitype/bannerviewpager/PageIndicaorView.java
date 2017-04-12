package com.recycleview.multitype.bannerviewpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.recycleview.multitype.R;


/*
 * ViewPager页面指示器
 */
public class PageIndicaorView extends RadioGroup {

    private ViewPager viewPager;

    /**
     * item count
     */
    private int itemCount;

    public PageIndicaorView(Context context) {
        this(context, null);
    }

    public PageIndicaorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageIndicaor();
    }

    private void initPageIndicaor() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (null == viewPager) {
                    return;
                }
                viewPager.setCurrentItem(checkedId);
            }
        });
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;

        initViewPager();
    }

    private void initViewPager() {
        if (!isValid()) {
            return;
        }
        viewPager.addOnPageChangeListener(pageChangeListener);
        itemCount = viewPager.getAdapter().getCount();

        createPageIndicaor();
    }

    /**
     * 判断ViewPager是否已经初始化
     *
     * @return true OK
     */
    private boolean isValid() {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager can not be NULL!");
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager adapter can not be NULL!");
        }
        return true;
    }

    /**
     * 创建初始化
     */
    private void createPageIndicaor() {
        removeAllViews();

        if (itemCount <= 0) {
            return;
        }

        for (int position = 0; position < itemCount; position++) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.view_radiobutton, null);
            radioButton.setId(position);
            radioButton.setText("");

            Drawable drawable;
            if (Build.VERSION.SDK_INT >= 22) {
                drawable = getContext().getDrawable(R.drawable.home_indicaor_selector);
            } else {
                drawable = getContext().getResources().getDrawable(R.drawable.home_indicaor_selector);
            }

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            radioButton.setCompoundDrawables(null, null, null, drawable);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(radioButton, params);
            radioButton.setChecked(position == 0 ? true : false);
        }
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            position--;
            if (position < 0 || position >= getChildCount()) {
                position = 0;
            }

            if (!(getChildAt(position) instanceof RadioButton)) {
                return;
            }

            RadioButton radioButton = (RadioButton) getChildAt(position);
            if (null == radioButton) {
                return;
            }

            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}