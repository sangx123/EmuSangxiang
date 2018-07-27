package com.sangxiang.android.work

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.network.param.ContactsParam
import com.sangxiang.android.work.UserSearchAdapter.CallListenter
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_search.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.concurrent.TimeUnit

class UserSearchActivity : BaseActivity() {
    lateinit var mAdapter: UserSearchAdapter
    var list:ArrayList<ContactsResult.UserItem> = ArrayList()
    companion object {
        val RESULT_CODE=117
        val param_select_users="param_select_users"
        val EXTRA_RESULT_ITEMS="EXTRA_RESULT_ITEMS"
        val EXTRA_RESULT_ITEMS_KEYWORDS="EXTRA_RESULT_ITEMS_KEYWORDS"
        val param_data_multipleSelect="param_data_multipleSelect"
        val param_keyword="param_keyword"
        fun toActivity(context: Context, list:ArrayList<ContactsResult.UserItem>,multipleSelect:Boolean,keyword:String,requestCode:Int){
            var intent=Intent(context,UserSearchActivity::class.java)
            intent.putExtra(param_data_multipleSelect,multipleSelect)
            intent.putExtra(param_select_users,list)
            intent.putExtra(param_keyword,keyword)
            (context as BaseActivity).startActivityForResult(intent,requestCode)
        }
    }
    var multipleSelect=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search)
        multipleSelect=intent.getBooleanExtra(param_data_multipleSelect,false)
        intent.getSerializableExtra(param_select_users)?.let {
            list=it as ArrayList<ContactsResult.UserItem>
        }
        intent.getStringExtra(param_keyword)?.let {
            etSearchBar.setText(it)
        }
        mHasNoData.visibility=View.GONE
        mHasData.visibility=View.GONE
        mAdapter=UserSearchAdapter(multipleSelect,list)
        mAdapter.setCallListenter(object :CallListenter{
            override fun onClick(num: Int) {
                btnOk.text="(已选${num}人) 确定"
            }

        })
        tv_cancel.onClick {
            finish()
        }
        btnOk.onClick {
            var intent=Intent()
            intent.putExtra(EXTRA_RESULT_ITEMS,list)
            intent.putExtra(EXTRA_RESULT_ITEMS_KEYWORDS,etSearchBar.text.toString().trim())
            setResult(RESULT_CODE,intent)
            finish()
        }
        mRecyclerView.adapter=mAdapter
        RxTextView.textChanges(etSearchBar).
                 debounce(400,TimeUnit.MILLISECONDS)
                //.filter{t->t.toString().trim().isNotBlank()}  //不需要过滤空值
                .flatMap { t -> EmucooApiRequest.getApiService().searchByname(ContactsParam(keyword = t.toString())) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ContactsResult>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: ContactsResult) {
                        mHasNoData.visibility=View.VISIBLE
                        mHasData.visibility=View.VISIBLE
                        t?.let {
                            it.users?.let {
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
                                btnOk.text="(已选${it.filter { it.selected }.size}人) 确定"
                            }
                        }
                        if(t==null||t.users==null|| t.users!!.isEmpty()){
                            mHasData.visibility=View.GONE
                        }else{
                            mHasNoData.visibility=View.GONE
                        }

                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }
}
