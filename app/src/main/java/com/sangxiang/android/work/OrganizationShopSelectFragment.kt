package com.sangxiang.android.work


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sangxiang.android.EmucooBaseFragment

import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.network.param.ContactsParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_organization_dept_select.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OrganizationShopSelectFragment : EmucooBaseFragment(), AnkoLogger {
    var list:ArrayList<ContactsResult.UserItem> =ArrayList()
    companion object {
        val param_data_multipleSelect="param_data_multipleSelect"
        val param_select_users="param_select_users"
        val param_contact_type="param_contact_type"
        fun newInstance(list:ArrayList<ContactsResult.UserItem>,multipleSelect:Boolean,contactType:Int): OrganizationShopSelectFragment {
            var fragment= OrganizationShopSelectFragment()
            val bundle = Bundle()
            bundle.putBoolean(param_data_multipleSelect,multipleSelect)
            bundle.putSerializable(param_select_users,list)
            bundle.putInt(param_contact_type,contactType)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var mAdapter: OrganizationShopSelectAdapter
    var multipleSelect=false
    var contactType=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization_shop_select, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            multipleSelect=it.getBoolean(param_data_multipleSelect,false)
            it.getSerializable(param_select_users)?.let {
                list=it as ArrayList<ContactsResult.UserItem>
            }
            contactType=it.getInt(param_contact_type,0)
        }
        initData()

    }

    private fun initData() {
        mAdapter= OrganizationShopSelectAdapter(multipleSelect,list,contactType)
        mRecyclerView.adapter=mAdapter
        EmucooApiRequest.getApiService().searchByshop(ContactsParam(contactType = contactType))
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
                            t.areas?.let {
                                mAdapter.setData(it,false)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }


}
