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
import com.sangxiang.android.utils.recycleView.HorizontalDividerItemDecoration
import com.squareup.picasso.Picasso
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_recycle_view.*
import kotlinx.android.synthetic.main.item_organization_shop_user_select.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.ArrayList

class RecycleViewActivity :BaseActivity() {
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
//                mName.text=item.name
//                if(!item.imgUrl.isNullOrBlank())
//                Picasso.with().load(item.imgUrl).into(mImage)
                helper.setText(R.id.mName,item.name)
               // var text=helper.getView<TextView>(R.id.mName)
            }

        }
        var notDataView = layoutInflater.inflate(R.layout.empty_view, mRecyclerView.parent as ViewGroup, false)
        mAdapter.emptyView = notDataView
        notDataView.onClick {
            initData()
        }
        mRecyclerView.adapter=mAdapter
        mRecyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).showLastDivider().build())

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
                                mList= it as ArrayList<ContactsResult.UserItem>?
                                mAdapter.setNewData(mList)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

}
