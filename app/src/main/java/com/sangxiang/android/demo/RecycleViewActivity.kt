package com.sangxiang.android.demo

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.network.param.ContactsParam
import com.sangxiang.android.utils.recycleView.RecycleViewHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_recycle_view.*
import kotlin.collections.ArrayList

class RecycleViewActivity :BaseActivity() {
    lateinit var mAdapter:BaseQuickAdapter<ContactsResult.UserItem,BaseViewHolder>
    var mList:ArrayList<ContactsResult.UserItem>? = null
    private lateinit var mRecycleViewHelper:RecycleViewHelper<ContactsResult.UserItem>
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
        mRecyclerView.adapter=mAdapter
//        mRecycleViewHelper=RecycleViewHelper(this,mRecyclerView,mSwipeRefreshLayout,true)
//        mRecycleViewHelper.setRecycleViewEmptyDataClickListener {
//            initData()
//        }

    }


    private fun initData() {
        EmucooApiRequest.getApiService().searchBydept(ContactsParam(deptId = 14))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<ContactsResult>>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)

                    }
                    override fun onNext(t: BaseResult<ContactsResult>) {
                        t.data?.let {
                            t.data!!.users?.let {
                                it as ArrayList<ContactsResult.UserItem>?
                                //mRecycleViewHelper.onApiSuccess(it)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mRecycleViewHelper.onApiError()

                    }

                })
    }

}
