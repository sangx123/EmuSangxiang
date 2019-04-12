package com.xiang.one.utils.recycleView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiang.one.R;
import com.xiang.one.network.model.BaseResult;

import java.util.List;

public class RecycleViewHelper<T> {
     boolean isRefresh=true;
     private Activity mActivity;
     private RecyclerView mRecyclerView;
     private BaseQuickAdapter<T,BaseViewHolder> mAdapter;
     private RecycleViewListener mRecycleViewListener=null;
     private SwipeRefreshLayout mSwipeRefreshLayout;
     private boolean needLoadMore=false;
     //默认10个为一页
     private int pageSize=10;
     private int pageIndex=1;

    public RecycleViewHelper(Activity context, RecyclerView mRecyclerView,SwipeRefreshLayout mSwipeRefreshLayout){
         this(context,mRecyclerView,mSwipeRefreshLayout,false,null);
     }

    /**
     * @param context
     * @param mRecyclerView
     * @param mSwipeRefreshLayout  外层是否有刷新组件
     * @param needLoadMore         是否有加载更多
     * @param mRecycleViewListener RecycleView的事件
     */
    public RecycleViewHelper(Activity context, RecyclerView mRecyclerView,SwipeRefreshLayout mSwipeRefreshLayout,boolean needLoadMore,RecycleViewListener mRecycleViewListener){
        this.mActivity=context;
        this.mRecyclerView=mRecyclerView;
        this.mAdapter=(BaseQuickAdapter<T,BaseViewHolder>)mRecyclerView.getAdapter();
        this.mSwipeRefreshLayout=mSwipeRefreshLayout;
        this.needLoadMore=needLoadMore;
        this.mRecycleViewListener=mRecycleViewListener;
        initRecycleView();
    }

    private void initRecycleView() {
        //设置空布局的点击事件
        addRecycleViewEmptyLayout();
        //初始化加载更多
        if(needLoadMore){
            initRecycleViewLoadMoreLayout();
        }
        mRecyclerView.addItemDecoration(setHorizontalDividerItemDecoration());
        //如果存在SwipeRefreshLayout赋予刷新的功能
        if(mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setColorSchemeResources(R.color.default_theme_orange, R.color.default_theme_green, R.color.default_theme_pink, R.color.default_theme_blue, R.color.default_theme_purple);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onRefreshs();
                }
            });
        }
        //初始化的时候默认加载数据
        onRefreshs();
    }

    public RecycleViewHelper(Activity context, RecyclerView mRecyclerView){
         this(context,mRecyclerView,null);
    }
    public  HorizontalDividerItemDecoration setHorizontalDividerItemDecoration(boolean showLastDivider){
        Paint paint = new Paint();
        paint.setStrokeWidth(5f);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        if(showLastDivider)
           return new HorizontalDividerItemDecoration.Builder(mActivity).paint(paint).showLastDivider().build();
        else {

           return new HorizontalDividerItemDecoration.Builder(mActivity).paint(paint).build();
        }
    }
    public  HorizontalDividerItemDecoration setHorizontalDividerItemDecoration(){
       return setHorizontalDividerItemDecoration(true);
    }

    public  void addRecycleViewEmptyLayout(){
        //添加空数据布局
        View emptyView=mActivity.getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup)mRecyclerView.getParent()  , false);
        mAdapter.setEmptyView(emptyView);
        emptyView.findViewById(R.id.mEmptyContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecycleViewListener!=null){
                    isRefresh=true;
                    mRecycleViewListener.getApiData(1,pageSize);
                }
            }
        });
    }


    public interface RecycleViewListener{
        void getApiData(int pageIndex,int pageSize);
    }




    public  void initRecycleViewLoadMoreLayout() {
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(new MRequestLoadMoreListener());
    }

    class MRequestLoadMoreListener implements BaseQuickAdapter.RequestLoadMoreListener {
        @Override
        public void onLoadMoreRequested() {
            loadMore();
        }

        MRequestLoadMoreListener(){

        }

    }

    private void loadMore() {
        isRefresh=false;
        if(mRecycleViewListener!=null);
        mRecycleViewListener.getApiData(pageIndex+1,pageSize);
    }


    public void onRefreshs() {
         isRefresh=true;
         mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
         if(mRecycleViewListener!=null);
         mRecycleViewListener.getApiData(1,pageSize);
    }

    public void onApiSuccess(BaseResult result,List<T> list){
         pageIndex=result.getPageNumber();
         pageSize=result.getPageSize();
         if(mSwipeRefreshLayout!=null&&needLoadMore==false){
             //如果只有刷新的话,没有加载更多的话
             mSwipeRefreshLayout.setRefreshing(false);
             mAdapter.setNewData(list);
         }else if(mSwipeRefreshLayout!=null&&needLoadMore==true){
             //如果有刷新有加载更多的话，如果是下拉刷新或者初始加载数据的话
             if(isRefresh){
                 //如果第一页是最后一页的话，不再加载更多
                 mSwipeRefreshLayout.setRefreshing(false);
                 mAdapter.setNewData(list);
                 if(result.isLastPage()){
                    mAdapter.setEnableLoadMore(false);
                 }else {
                    mAdapter.setEnableLoadMore(true);
                    //默认第一次加载会进入回调，如果不需要可以配置：(如果有数量10条的限制的话此处用不到了)
                    //mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
                 }

             }else {
                 setMoreData(result,list);
             }

         }else if(mSwipeRefreshLayout==null&&needLoadMore==false){
             //没有下拉刷新而且没有加载更多的话
             mAdapter.setNewData(list);
         }
    }

    public void onApiError(){
        if(mSwipeRefreshLayout!=null&&needLoadMore==false){
            //如果只有刷新的话,没有加载更多的话
            mSwipeRefreshLayout.setRefreshing(false);
        }else if(mSwipeRefreshLayout!=null&&needLoadMore==true){
            //如果有刷新有加载更多的话，如果是下拉刷新或者初始加载数据的话
            if(isRefresh){
                mSwipeRefreshLayout.setRefreshing(false);
            }else{
                mAdapter.loadMoreFail();
            }
        }else if(mSwipeRefreshLayout==null&&needLoadMore==false){
            //没有下拉刷新而且没有加载更多的话
        }
    }

    private void setMoreData(BaseResult result,List<T> data) {
        int size = data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (result.isLastPage()) {
            //如果是最后一页的话显示，数据全部加载完毕
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        mRecyclerView.invalidateItemDecorations();
    }

}
