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
import kotlinx.android.synthetic.main.activity_organization_dept_user_select.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.startActivity

class OrganizationDeptUserSelectActivity : BaseActivity() {

    companion object {
        val RESULT_CODE=115
        var param_data_deptid="param_data_deptid"
        var param_data_multipleSelect="param_data_multipleSelect"
        val param_select_users="param_select_users"
        var param_title="param_title"
        val param_contact_type="param_contact_type"
        fun toActivity(context: Context,id:Long,title:String?,  list:ArrayList<ContactsResult.UserItem> ,multipleSelect:Boolean
        ,contactType:Int,requestCode:Int){
            var intent=Intent(context,OrganizationDeptUserSelectActivity::class.java)
            intent.putExtra(param_data_deptid,id)
            intent.putExtra(param_data_multipleSelect,multipleSelect)
            intent.putExtra(param_select_users,list)
            intent.putExtra(param_title,title)
            intent.putExtra(param_contact_type,contactType)
            (context as BaseActivity).startActivityForResult(intent,requestCode)
        }
    }
    var list:ArrayList<ContactsResult.UserItem> =ArrayList()
    var deptid:Long=0
    var multipleSelect=false
    var title:String?=null
    var contactType=0
    lateinit var mAdapter: OrganizationDeptUserSelectAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organization_dept_user_select)
        setStatusBarDefaultColor()
        deptid=intent.getLongExtra(param_data_deptid,0)
        contactType=intent.getIntExtra(param_contact_type,0)
        multipleSelect=intent.getBooleanExtra(param_data_multipleSelect,false)
        intent.getSerializableExtra(param_select_users)?.let {
            list=it as ArrayList<ContactsResult.UserItem>
        }
        title=intent.getStringExtra(param_title)
        initView()
        initData()
    }



    private fun initData() {
        EmucooApiRequest.getApiService().searchBydept(ContactsParam(deptId = deptid,contactType = contactType))
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
                                for(item0 in it) {
                                        list?.let {
                                            for (item1 in it){
                                                if(item0.id==item1.id){
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
            var intent = Intent()
            intent.putExtra(OrganizationSelectActivity.EXTRA_RESULT_ITEMS, list)
            setResult(RESULT_CODE, intent)
            finish()
        })
        mAdapter=OrganizationDeptUserSelectAdapter(multipleSelect, list)
        //mRecyclerView.addItemDecoration(MItemDecoration(1, 0,dip(1),dip(1)))
        mRecyclerView.adapter=mAdapter
    }
}
