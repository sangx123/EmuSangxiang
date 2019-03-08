package com.sangxiang.android.ui.taobao


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sangxiang.android.EmucooBaseFragment

import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.PageInfo
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.utils.recycleView.RecycleViewHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tao_bao.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TaoBaoFragment : EmucooBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
       return inflater.inflate(R.layout.fragment_tao_bao, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    lateinit var mAdapter:BaseQuickAdapter<TaobaoTask,BaseViewHolder>
    var mList:ArrayList<TaobaoTask>? = null
    private lateinit var mRecycleViewHelper:RecycleViewHelper<TaobaoTask>

    fun initData() {
        mPublishTaskLayout.onClick {
            startActivity<PublishTaoBaoTaskActivity>()
        }

        mPublishTaskHistoryLayout.onClick {
            startActivity<PublishTaoBaoTaskHistoryActivity>()
        }

        mMyTaskLayout.onClick {
            startActivity<MyTaoBaoTaskActivity>()
        }

        mAdapter=object : BaseQuickAdapter<TaobaoTask, BaseViewHolder>(R.layout.item_taobao_home_task,mList) {
            override fun convert(helper: BaseViewHolder, item: TaobaoTask) {
                helper.setText(R.id.mName,item.shuadanyongjin.toString())
                      .setText(R.id.mPrice,item.shangpinjiage.toString())
            }

        }
        mRecyclerView.adapter=mAdapter
        mRecycleViewHelper= RecycleViewHelper(activity,mRecyclerView,mSwipeRefreshLayout,true, RecycleViewHelper.RecycleViewListener { pageIndex, pageSize ->
            var taoBaoHomeTaskParam=TaoBaoHomeTaskParam()
            taoBaoHomeTaskParam.pageSize=pageSize
            taoBaoHomeTaskParam.pageNumber=pageIndex
            EmucooApiRequest.getApiService().getTaoBaoHomeTask(taoBaoHomeTaskParam)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResult<List<TaobaoTask>>> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                            mDisposables.add(d)
                        }

                        override fun onNext(t: BaseResult<List<TaobaoTask>>) {
                            mRecycleViewHelper.onApiSuccess(t,t.data)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            mRecycleViewHelper.onApiError()
                        }

                    })
        })

    }
}
