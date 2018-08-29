package com.sangxiang.android.utils.recycleView;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sangxiang.android.R;

import java.util.List;

public class RecycleViewHelper<T> {

     int pageSize = 6;
     int pageIndex=1;
     boolean isRefresh=true;
     private Activity mActivity;
     private RecyclerView mRecyclerView;
     private BaseQuickAdapter<T,BaseViewHolder> mAdapter;
     private RecycleViewListener mRecycleViewListener=null;
     private SwipeRefreshLayout mSwipeRefreshLayout;
     private boolean needLoadMore=false;
     public RecycleViewHelper(Activity context, RecyclerView mRecyclerView,SwipeRefreshLayout mSwipeRefreshLayout){
         this(context,mRecyclerView,mSwipeRefreshLayout,false);
     }

    public RecycleViewHelper(Activity context, RecyclerView mRecyclerView,SwipeRefreshLayout mSwipeRefreshLayout,boolean needLoadMore){
        this.mActivity=context;
        this.mRecyclerView=mRecyclerView;
        this.mAdapter=(BaseQuickAdapter<T,BaseViewHolder>)mRecyclerView.getAdapter();
        this.mSwipeRefreshLayout=mSwipeRefreshLayout;
        this.needLoadMore=needLoadMore;
    }
    public int getPageIndex() {
        return pageIndex;
    }
    public RecycleViewHelper(Activity context, RecyclerView mRecyclerView){
         this(context,mRecyclerView,null);
    }
    public  HorizontalDividerItemDecoration getHorizontalDividerItemDecoration(boolean showLastDivider){
        if(showLastDivider)
           return new HorizontalDividerItemDecoration.Builder(mActivity).showLastDivider().build();
        else {
           return new HorizontalDividerItemDecoration.Builder(mActivity).build();
        }
    }
    public  HorizontalDividerItemDecoration getHorizontalDividerItemDecoration(){
       return getHorizontalDividerItemDecoration(true);
    }

    public  void initRecycleViewEmptyLayout(){
        //添加空数据布局
        View emptyView=mActivity.getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup)mRecyclerView.getParent()  , false);
        mAdapter.setEmptyView(emptyView);
        emptyView.findViewById(R.id.mEmptyContainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRecycleViewListener!=null){
                    pageIndex=1;
                    isRefresh=true;
                    mRecycleViewListener.getApiData();
                }
            }
        });
    }

    public void setRecycleViewEmptyDataClickListener(RecycleViewListener mRecycleViewListener) {
        this.mRecycleViewListener = mRecycleViewListener;
        //初始化空布局
        initRecycleViewEmptyLayout();
        //初始化加载更多
        if(needLoadMore){
            initRecycleViewLoadMoreLayout();
        }
        mRecyclerView.addItemDecoration(getHorizontalDividerItemDecoration());
        //如果存在SwipeRefreshLayout赋予刷新的功能
        if(mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    onRefreshs();
                }
            });
        }
    }


    public interface RecycleViewListener{
        void getApiData();
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
        mRecycleViewListener.getApiData();
    }


    public void onRefreshs() {
         isRefresh=true;
         pageIndex=1;
         mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
         if(mRecycleViewListener!=null);
         mRecycleViewListener.getApiData();
    }

    public void onApiSuccess(List<T> list){
         if(mSwipeRefreshLayout!=null&&needLoadMore==false){
             //如果只有刷新的话,没有加载更多的话
             mSwipeRefreshLayout.setRefreshing(false);
             mAdapter.setNewData(list);
         }else if(mSwipeRefreshLayout!=null&&needLoadMore==true){
             //如果有刷新有加载更多的话，如果是下拉刷新或者初始加载数据的话
             if(isRefresh){
                 mAdapter.setEnableLoadMore(true);
                 mSwipeRefreshLayout.setRefreshing(false);
             }
             setMoreData(list);
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

    private void setMoreData(List<T> data) {
        int size = data.size();
        if (isRefresh) {
            pageIndex++;
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
                pageIndex++;
            }
        }
        if (size < pageSize) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
        } else {
            mAdapter.loadMoreComplete();
        }
        mRecyclerView.invalidateItemDecorations();
    }

}
