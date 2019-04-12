package com.xiang.one.ui.home


import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiang.one.BaseFragment

import com.xiang.one.R
import com.xiang.one.network.Api
import com.xiang.one.network.error.RxUtils
import com.xiang.one.network.model.BaseResult
import com.xiang.one.ui.taobao.TaoBaoHomeTaskParam
import com.xiang.one.ui.taobao.TaobaoTask
import com.xiang.one.utils.DisplayUtil
import com.xiang.one.utils.recycleView.RecycleViewHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Response
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return  inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    lateinit var mAdapter: BaseQuickAdapter<TaobaoTask, BaseViewHolder>
    var mList:ArrayList<TaobaoTask>? = null
    private lateinit var mRecycleViewHelper: RecycleViewHelper<TaobaoTask>

    fun initData() {
        //初始化banner
        var list= ArrayList<BannerBean>()
        var model= BannerBean()
        model.imageUrl="http://www.vz18.com/upload/201607/15/201607152321397655.png"
        list.add(model)
        model= BannerBean()
        model.imageUrl="http://www.vz18.com/upload/201607/15/201607152333240637.png"
        list.add(model)
        mBannerContainer.removeAllViews()
        mBannerContainer.addView(addBannerView(list, 5 * 1000))

        //初始化recycleview
        mAdapter=object : BaseQuickAdapter<TaobaoTask, BaseViewHolder>(R.layout.item_taobao_home_task,mList) {
            override fun convert(helper: BaseViewHolder, item: TaobaoTask) {
                helper.setText(R.id.mName,item.shuadanyongjin.toString())
                        .setText(R.id.mPrice,item.shangpinjiage.toString())
            }
        }
        mRecyclerView.adapter=mAdapter
        mRecycleViewHelper= RecycleViewHelper(activity,mRecyclerView,mSwipeRefreshLayout,true, RecycleViewHelper.RecycleViewListener { pageIndex, pageSize ->
            var taoBaoHomeTaskParam= TaoBaoHomeTaskParam()
            taoBaoHomeTaskParam.pageSize=pageSize
            taoBaoHomeTaskParam.pageNumber=pageIndex
            Api.getApiService().getTaoBaoHomeTask(taoBaoHomeTaskParam)
                    .compose(RxUtils.handleGlobalError<BaseResult<List<TaobaoTask>>>(activity!!))
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

    //设置banner
    private fun addBannerView(banners: List<BannerBean>, bannerInterval: Int): View {
        // calculate banner height
        val displayUtil = DisplayUtil(context)
        val width = displayUtil.screenWidth
        val height = width * 172 / 375
        val view = LayoutInflater.from(context).inflate(R.layout.banner_view, null, false)
        val banner = view.findViewById(R.id.dashboard_banner_raw) as BGABanner
        banner.layoutParams.height = height
        val imageUrls = ArrayList<String>(banners.size)
        val contentStrs = ArrayList<String>(banners.size)

        for (item in banners){
            contentStrs.add("")
            imageUrls.add(item.imageUrl)
        }

        banner.setAdapter { banner, itemView, model, position ->
            val actionItem = banners[position]
            if (!TextUtils.isEmpty(actionItem.imageUrl)&& itemView is ImageView)
                Glide.with(activity!!)
                        .load(actionItem.imageUrl).into(itemView)
        }
        //Log.e("sangxiang", "addBannerView: "+banners.size());
        banner.setAutoPlayAble(banners.size > 1)
        banner.setData(imageUrls, contentStrs)
        banner.setDelegate { banner, itemView, model, position ->
            val actionItem = banners[position]
            //点击往后台传递数据
//            val call = KaishiApp.getApiService().insertBannerView(BannerView(actionItem.objId, Settings.Global.getMe().getId()))
//            call.enqueue(object : KaishiCallback<String>(callList, context, true) {
//                protected fun success(response: Response<String>) {}
//
//                protected fun dismissProgress() {}
//            })
//            callList.add(call)

            // go to target screen
        }

        banner.setAutoPlayInterval(bannerInterval)
        banner.startAutoPlay()
        return view
    }

}
