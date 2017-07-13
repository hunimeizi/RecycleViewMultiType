# MultiType
</br>
根据大神drakeet实现RecycleView的Item多类型demo进行改变，大神gitub地址为（https://github.com/drakeet/MultiType)
</br>

# 总览</br>
  ![github](https://raw.githubusercontent.com/hunimeizi/RecycleViewMultiType/master/app/src/main/res/mipmap-hdpi/zonglan_iamge.jpg "github")
 
# MultiType基础用法</br>

### 引入</br>

在你的 build.gradle:</br></br>

    dependencies
    {
        compile 'me.drakeet.multitype:multitype:2.4.0'
    }

注：MultiType 内部引用了 recyclerview-v7:25.1.0，如果你不想使用这个版本，
可以使用 exclude 将它排除掉，再自行引入你选择的版本。
示例如下：

    dependencies
    {
    compile('me.drakeet.multitype:multitype:2.4.0',
        {
            exclude group: 'com.android.support'
        })
    compile 'com.android.support:recyclerview-v7:你选择的版本'
    }

### 不用以上“引入”模块，可添加该包以下工具类  
        com.recycleview.multitype.multitype
        
### 使用</br>
##### 1.创建一个 class，它将是你的数据类型或 Java bean/model. 对这个类的内容没有任何限制。示例如下：</br>

    public class Title
    {
      @NonNull public String title;
      public Activity1(@NonNull final String title)
        {
          this.title = title;
        }
     }
  </br>

##### 2.创建一个 class 继承 ItemViewProvider</br>
  ItemViewProvider 是个抽象类，其中 onCreateViewHolder 方法用于生产你的 Item View Holder, onBindViewHolder 用于绑定数据到 Views. 一般一个 ItemViewProvider 类在内存中只会有一个实例对象，MultiType 内部将复用这个 provider 对象来生产所有相关的 Item Views 和绑定数据。示例：
</br>

        public class TitleProvider extends ItemViewProvider<Title, TitleProvider.ViewHolder>
        {
            @NonNull
            @Override
          protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent)
            {
                View root = inflater.inflate(R.layout.item_title, parent, false);
                return new ViewHolder(root);
            }

         @Override
        protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Title title)
           {
                holder.tv_sectionDivide_left.setText(title.title);
           }

        static class ViewHolder extends RecyclerView.ViewHolder
          {
            TextView tv_sectionDivide_left;
            ViewHolder(View itemView) {
            super(itemView);
            tv_sectionDivide_left = (TextView) itemView.findViewById(R.id.tv_sectionDivide_left);
          }
        }
       }
</br>

##### 3.在 Activity 中加入 RecyclerView 和 List 并注册你的类型
        adapter = new MultiTypeAdapter(items);
         /* 注册类型和 View 的对应关系 */
        adapter.register(Title.class, new TitleProvider());
        items.add(new Title("便捷生活"));
        loadMore_multiTypeRecycleView.setAdapter(adapter);
</br>

##### 4.实现了RecycleView嵌套RecycleView横向滚动
       recyclerView = (RecyclerView) itemView.findViewById(R.id.post_list);
       LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
       layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
       recyclerView.setLayoutManager(layoutManager);
       adapter = new PostsAdapter();
       recyclerView.setAdapter(adapter);
  详细见 HorizontalPostsViewProvider类
##### 5.实现RecycleView上拉加载更多操作并实现item点击postion问题
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

