package com.emucoo.business_manager.widgets

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.emucoo.business_manager.App
import com.emucoo.business_manager.network.Constants
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor

/**
 * 27/01/2018  5:12 PM
 * Created by Zhang.
 * 客户需重写 onLoadMoreCount()
 * onLoadMoreItemViewType()
 * onCreateLoadMoreViewHolder()
 * onBindViewHolder
 *
 */
abstract class LoadMoreAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(), MoreData<T> {
    private val tag = "LoadMoreAdapter"
    private val TAG = tag

    val LOAD_MORE_PROGRESS = 39341;
    val mData = ArrayList<T>()
    var mHasMore: Boolean = true;

    public var mLoadMoreAdapterTotalSize = 0

    /**
     * 对于有多种类型的View存在于同一个RecycleView中
     * 这个数据结构需要在 onLoadMoreCount 中正向填充
     * 可以极大的简化onLoadMoreItemViewType 中获取view 类型 和 onLoadMoreBindViewHolder 获取该类型Posititon的难度
     * (也就是说 onLoadMoreItemViewType 的方法中不需要在反向推演，某个位置是什么类型的view)
     */
    val mViewPositionMap = mutableMapOf<Int/*在整个列表中的位置*/, Triple<Int/*类型*/, Int/*在list中的位置，如果是表头表尾那么可以是0*/, T?/*列表具体的item,如果是表头，那么为null*/>>()

    var mTotalIndex: Int = 0  //总的索引，使用在count中


    override fun addMoreData(data: List<T>?, hasMore: Boolean) {
        mHasMore = hasMore
        if (data != null && data.size > 0) {
            mData.addAll(data)
            notifyItemRangeChanged(mData.size - data.size, data.size)
        } else {
            notifyItemChanged(mData.size - 1)
        }
    }

    override fun setData(data: List<T>?, hasMore: Boolean) {
        mHasMore = hasMore
        if (data != null) {
            mData.clear()
            if(data.size > 0) {
                mData.addAll(data)
            }
            notifyDataSetChanged()
        }
    }

    /**
     * 返回个数
     */
    open fun onLoadMoreCount(): Int {
        return mData.size
    }

    /**
     * view的类型
     */
    open fun onLoadMoreItemViewType(position: Int): Int {
        return  Constants.TYPE_ITEM
    }

    /**
     * 创建view
     */
    abstract fun onCreateLoadMoreViewHolder(parent: ViewGroup, viewType: Int): ViewHolder?

    @Deprecated("use onCreateLoadMoreViewHolder", ReplaceWith("onCreateLoadMoreViewHolder"))
    open override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {

        if (viewType == LOAD_MORE_PROGRESS) {
            val textView = TextView(parent.context);
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.getInstance().dip(45f))
            textView.gravity = Gravity.CENTER
            textView.textColor = Color.BLACK
            textView.textSize = 16f
            textView.text = "加载中..."
            textView.layoutParams = params
            return LoadMoreProgress(textView)
        }
        return onCreateLoadMoreViewHolder(parent, viewType)
    }

    class LoadMoreProgress(itemView: View?) : ViewHolder(itemView)

    @Deprecated("不要覆盖该方法，使用getCount（）传递子view个数", ReplaceWith("onLoadMoreCount"))
    override fun getItemCount(): Int {
        if (mHasMore) {
            mLoadMoreAdapterTotalSize = onLoadMoreCount() + 1  //加载中的条目占一个Item位置
        } else {
            mLoadMoreAdapterTotalSize =onLoadMoreCount()
        }
        return mLoadMoreAdapterTotalSize
    }

    @Deprecated("不要覆盖该方法，使用getViewType（）传递子view的类型", replaceWith = ReplaceWith("onLoadMoreItemViewType"))
    override fun getItemViewType(position: Int): Int {
        /*
        if (position == onLoadMoreCount()) {
            return LOAD_MORE_PROGRESS
        }
        */
        if(position == if(mHasMore) (mLoadMoreAdapterTotalSize-1) else (mLoadMoreAdapterTotalSize)){
            return LOAD_MORE_PROGRESS
        }
        return onLoadMoreItemViewType(position)
    }

    @Deprecated("请使用onLoadMoreBindViewHolder", ReplaceWith("onLoadMoreBindViewHolder"))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onLoadMoreBindViewHolder(holder, position)
    }

    /**
     * 绑定数据
     */
    abstract fun onLoadMoreBindViewHolder(holder: ViewHolder, position: Int)
}