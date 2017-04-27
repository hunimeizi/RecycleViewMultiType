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
import com.recycleview.multitype.item.Activity_item;
import com.recycleview.multitype.item.Banner;
import com.recycleview.multitype.item.BannerProvider;
import com.recycleview.multitype.item.GuessGrid;
import com.recycleview.multitype.item.GuessGridProvider;
import com.recycleview.multitype.item.HorizontalPostsViewProvider;
import com.recycleview.multitype.item.PostList;
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
        List<String> list = new ArrayList<>();
        list.add("充话费");
        list.add("充流量");
        list.add("团购");
        list.add("打车");
        list.add("酒店");
        list.add("美食");
        list.add("电影");
        list.add("机票");
        List<String> list_url = new ArrayList<>();
        list_url.add("http://img4.imgtn.bdimg.com/it/u=3269960449,991534647&fm=23&gp=0.jpg");
        list_url.add("http://img5.imgtn.bdimg.com/it/u=4155127908,3386565855&fm=23&gp=0.jpg");
        list_url.add("http://img0.imgtn.bdimg.com/it/u=3192885799,3600275175&fm=23&gp=0.jpg");
        list_url.add("http://img3.imgtn.bdimg.com/it/u=4131057487,2760357258&fm=23&gp=0.jpg");
        list_url.add("http://img2.imgtn.bdimg.com/it/u=3177943619,3669422245&fm=23&gp=0.jpg");
        list_url.add("http://img0.imgtn.bdimg.com/it/u=990986148,443719176&fm=23&gp=0.jpg");
        list_url.add("http://img4.imgtn.bdimg.com/it/u=78088078,2856149958&fm=23&gp=0.jpg");
        list_url.add("http://img5.imgtn.bdimg.com/it/u=1741358067,4251507804&fm=23&gp=0.jpg");
        Activity_item activity_item = new Activity_item(list_url, list);
        List<Activity_item> activity1List = new ArrayList<>();
        activity1List.add(activity_item);
        items.add(new Activity1(activity1List));
        items.add(new Title("产品推荐"));
        for (int i = 0; i < 5; i++) {
            items.add(new Activity2());
        }
        items.add(new Title("美人福利"));
        items.add(new PostList(list_url));
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
        adapter.register(PostList.class, new HorizontalPostsViewProvider());
        adapter.register(Activity3.class, new Activity3Provider());
        adapter.register(GuessGrid.class, new GuessGridProvider());
    }

    private int mTempGuessDataSize;
    private int mOldGuessDataSize;

    public void getGuessLonkeyData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int oldSize = adapter.getItemCount();
                mOldGuessDataSize = mTempGuessDataSize;
                mTempGuessDataSize += 6;
                for (int i = 0; i < 6; i++) {
                    items.add(new GuessGrid(mOldGuessDataSize++));
                }
                adapter.notifyItemRangeInserted(oldSize, 6);
            }
        }, 1000);
    }
}
