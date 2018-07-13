package com.emucoo.business_manager.work

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.emucoo.business_manager.BaseActivity
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.EmucooApiRequest
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.network.param.ContactsParam
import com.emucoo.business_manager.widgets.MItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_organization_shop_user_select.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity
class OrganizationShopUserSelectActivity : BaseActivity() {

    var brandId:Long=0
    var areaId:Long=0
    var multipleSelect=false
    lateinit var mAdapter:OrganizationShopUserSelectAdapter
    var list:ArrayList<ContactsResult.UserItem> =ArrayList()
    var title:String?=null
    var contactType=0
    companion object {
        val param_data_brand_id="param_data_brand_id"
        val param_data_area_id="param_data_area_id"
        val param_data_multipleSelect="param_data_multipleSelect"
        val param_select_users="param_select_users"
        val param_title="param_title"
        val param_contact_type="param_contact_type"
        val RESULT_CODE=116
        fun toActivity(context: Context,brandId:Long,areaId:Long,title:String?,list:ArrayList<ContactsResult.UserItem>,multipleSelect:Boolean,
                       contactType:Int,requetCode:Int){
            var intent=Intent(context,OrganizationShopUserSelectActivity::class.java)
            intent.putExtra(param_data_brand_id,brandId)
            intent.putExtra(param_data_area_id,areaId)
            intent.putExtra(param_data_multipleSelect,multipleSelect)
            intent.putExtra(param_select_users,list)
            intent.putExtra(param_title,title)
            intent.putExtra(param_contact_type,contactType)
            (context as BaseActivity).startActivityForResult(intent,requetCode)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_shop_user_select)
        setStatusBarDefaultColor()
        brandId=intent.getLongExtra(param_data_brand_id,0)
        areaId=intent.getLongExtra(param_data_area_id,0)
        multipleSelect=intent.getBooleanExtra(param_data_multipleSelect,false)
        intent.getSerializableExtra(param_select_users)?.let {
            list=it as ArrayList<ContactsResult.UserItem>
        }
        title=intent.getStringExtra(param_title)
        contactType=intent.getIntExtra(param_contact_type,0)
        initView()
        initData()

    }

    private fun initData() {
        EmucooApiRequest.getApiService().searchByshop(ContactsParam(brandId = brandId,areaId = areaId,contactType = contactType))
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
                            t.shops?.let {
                                for(item0 in it) {

                                   list?.let {
                                            for (item1 in it){
                                                if(item0.managerId==item1.id){
                                                    item0.selected=true
                                                }
                                            }
                                        }
                                }
                                mAdapter.setData(it,false)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
    private fun initView() {
        if(!title.isNullOrBlank()){
            mToolbar.setTitle(title!!)
        }
        mToolbar.setRightOnClickListener(View.OnClickListener {

//            var list= ArrayList<ContactsResult.ShopItem>()
//            for(item in mAdapter.mData){
//                if(item.selected){
//                    list.add(item)
//                }
//            }
//            if(resultModel==null){
//                resultModel=ContactsResult()
//            }
//
//            if(!multipleSelect){
//                resultModel!!.users=null
//            }
//            resultModel!!.shops=list
            var intent=Intent()
            intent.putExtra(OrganizationSelectActivity.EXTRA_RESULT_ITEMS,list)
            setResult(RESULT_CODE,intent)
            finish()
        })
        mAdapter=OrganizationShopUserSelectAdapter(multipleSelect,list)
        mRecyclerView.addItemDecoration(MItemDecoration(1, 0,dip(1),dip(1)))
        mRecyclerView.adapter=mAdapter
    }
}
