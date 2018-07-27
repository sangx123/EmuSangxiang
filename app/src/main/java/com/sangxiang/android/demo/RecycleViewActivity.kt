package com.sangxiang.android.demo

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.R.id.mRecyclerView
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.network.param.ContactsParam
import com.sangxiang.android.utils.recycleView.CustomLoadMoreView
import com.sangxiang.android.utils.recycleView.HorizontalDividerItemDecoration
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_recycle_view.*
import kotlinx.android.synthetic.main.item_organization_shop_user_select.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast
import java.util.ArrayList

class RecycleViewActivity :BaseActivity() {
    private val PAGE_SIZE = 6
    lateinit var mAdapter:BaseQuickAdapter<ContactsResult.UserItem,BaseViewHolder>
    var mList:ArrayList<ContactsResult.UserItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)
        mToolbar.setRightOnClickListener(View.OnClickListener {
            mAdapter.setNewData(null)
        })
        mAdapter=object :BaseQuickAdapter<ContactsResult.UserItem,BaseViewHolder>(R.layout.item_organization_shop_user_select,mList) {
            override fun convert(helper: BaseViewHolder, item: ContactsResult.UserItem) {
                helper.setText(R.id.mName,item.name)
            }

        }

        var notDataView = layoutInflater.inflate(R.layout.empty_view, mRecyclerView.parent as ViewGroup, false)
        mAdapter.emptyView = notDataView
        notDataView.onClick {
            initData()
        }
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener { loadMore() }
        mRecyclerView.adapter=mAdapter
        mRecyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).showLastDivider().build())
        mSwipeRefreshLayout.onRefresh {
            refresh()
        }
    }

    private fun loadMore() {
        isRefresh=false
        initData()
    }

    private var mNextRequestPage = 1
    private fun setData(isRefresh: Boolean, data: MutableList<ContactsResult.UserItem>) {
        val size = data?.size
        if (isRefresh) {
            mNextRequestPage++
            mAdapter.setNewData(data)
        } else {
            if (size > 0) {
                mAdapter.addData(data)
                mNextRequestPage++
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh)
            toast("no more data")
        } else {
            mAdapter.loadMoreComplete()
        }
    }

    private fun initData() {
        EmucooApiRequest.getApiService().searchBydept(ContactsParam(deptId = 14))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ContactsResult> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)

                    }

                    override fun onNext(t: ContactsResult) {
                        t?.let {
                            t.users?.let {
                                it as ArrayList<ContactsResult.UserItem>?
                                if(isRefresh){
                                    mAdapter.setEnableLoadMore(true)
                                    mSwipeRefreshLayout.isRefreshing = false
                                }
                                setData(isRefresh, it)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        if(isRefresh){
                            mAdapter.setEnableLoadMore(true)
                            mSwipeRefreshLayout.isRefreshing = false
                        }else{
                            mAdapter.loadMoreFail()
                        }

                    }

                })
    }

    var isRefresh=true
    private fun refresh() {
        isRefresh=true
        mNextRequestPage = 1
        mAdapter.setEnableLoadMore(false)//这里的作用是防止下拉刷新的时候还可以上拉加载
        initData()
    }

}
