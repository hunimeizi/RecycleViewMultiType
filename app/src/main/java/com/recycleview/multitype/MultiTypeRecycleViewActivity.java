package com.recycleview.multitype;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.recycleview.multitype.item.Activity1;
import com.recycleview.multitype.item.Activity1Provider;
import com.recycleview.multitype.item.Activity2;
import com.recycleview.multitype.item.Activity2Provider;
import com.recycleview.multitype.item.Activity3;
import com.recycleview.multitype.item.Activity3Provider;
import com.recycleview.multitype.item.Banner;
import com.recycleview.multitype.item.BannerProvider;
import com.recycleview.multitype.item.GuessGrid;
import com.recycleview.multitype.item.GuessGridProvider;
import com.recycleview.multitype.item.Title;
import com.recycleview.multitype.item.TitleProvider;
import com.recycleview.multitype.loadmore.LoadMoreRecyclerView;
import com.recycleview.multitype.loadmore.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.MultiTypeAdapter;

public class MultiTypeRecycleViewActivity extends AppCompatActivity {


    private List<Object> items = new ArrayList<>();
    private MultiTypeAdapter adapter;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_type_recycle_view);
        final LoadMoreRecyclerView loadMore_multiTypeRecycleView = (LoadMoreRecyclerView) findViewById(R.id.loadMore_multiTypeRecycleView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //通过 setSpanSizeLookup 来告诉布局，item 占几个横向单位，如果横向有 2个单位，而你返回当前 item 占用 2个单位，
        // 那么它就会看起来单独占用一行如果返回1个单位，那么该布局就显示横向的一半
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (items.size() == 0 || items.size() <= position) {
                    return 2;
                }
                Object item = items.get(position);
                if (item instanceof GuessGrid) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        loadMore_multiTypeRecycleView.setLayoutManager(layoutManager);
        loadMore_multiTypeRecycleView.setHasFixedSize(true);
        adapter = new MultiTypeAdapter(items);
        registerProvider();
        items.add(new Banner());
        items.add(new Title("便捷生活"));
        items.add(new Activity1());
        items.add(new Title("产品推荐"));
        for (int i = 0; i < 5; i++) {
            items.add(new Activity2());
        }
        items.add(new Title("精彩推荐"));
        items.add(new Activity3());
        items.add(new Title("猜你喜欢"));
        loadMore_multiTypeRecycleView.setAdapter(adapter);
        loadMore_multiTypeRecycleView.setHasLoadMore(true);
        loadMore_multiTypeRecycleView.showLoadingUI();
        loadMore_multiTypeRecycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mPage++;
                if (mPage <= 5) {
                    getGuessLonkeyData();
                } else {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            loadMore_multiTypeRecycleView.goneLoadingUI();
                            loadMore_multiTypeRecycleView.visibleMoreBtn();
                            loadMore_multiTypeRecycleView.onClickLoadMore();
                        }
                    });
                }
            }
        });
    }

    private void registerProvider() {
        adapter.register(Banner.class, new BannerProvider());
        adapter.register(Title.class, new TitleProvider());
        adapter.register(Activity1.class, new Activity1Provider());
        adapter.register(Activity2.class, new Activity2Provider());
        adapter.register(Activity3.class, new Activity3Provider());
        adapter.register(GuessGrid.class, new GuessGridProvider());
    }
    public void getGuessLonkeyData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int oldSize = adapter.getItemCount();
                for (int i = 0; i < 6; i++) {
                    items.add(new GuessGrid());
                }
                adapter.notifyItemRangeInserted(oldSize, 6);
            }
        }, 1000);
    }
}
