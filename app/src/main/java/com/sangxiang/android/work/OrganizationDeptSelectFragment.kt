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
import com.sangxiang.android.widgets.MItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_organization_dept_select.*
import org.jetbrains.anko.error
import org.jetbrains.anko.support.v4.dip

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OrganizationDeptSelectFragment : EmucooBaseFragment() {
    companion object {
        val param_data_multipleSelect="param_data_multipleSelect"
        val param_select_users="param_select_users"
        val param_contact_type="param_contact_type"
        fun newInstance(list:ArrayList<ContactsResult.UserItem>,multipleSelect:Boolean,contactType:Int): OrganizationDeptSelectFragment {
            var fragment= OrganizationDeptSelectFragment()
            val bundle = Bundle()
            bundle.putBoolean(param_data_multipleSelect,multipleSelect)
            bundle.putSerializable(param_select_users,list)
            bundle.putInt(param_contact_type,contactType)
            fragment.setArguments(bundle)
            return fragment
        }
    }
    var list:ArrayList<ContactsResult.UserItem> =ArrayList()
    lateinit var mAdapter:OrganizationDeptSelectAdapter
    var multipleSelect=false
    var contactType=0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_organization_dept_select, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        arguments?.let {
            multipleSelect= it.getBoolean(param_data_multipleSelect,false)
            contactType=it.getInt(param_contact_type,0)
            it.getSerializable(param_select_users)?.let {
                list=it as ArrayList<ContactsResult.UserItem>
            }
        }

        mAdapter=OrganizationDeptSelectAdapter(multipleSelect,list,contactType)
        //mRecyclerView.addItemDecoration(MItemDecoration(1, 0,dip(1),dip(1)))
        mRecyclerView.adapter=mAdapter
        EmucooApiRequest.getApiService().searchBydept(ContactsParam(contactType=contactType))
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
                            t.depts?.let {
                                mAdapter.setData(t.depts,false)
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

}
