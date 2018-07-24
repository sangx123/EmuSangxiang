package com.sangxiang.android.work

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sangxiang.android.R
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.widgets.LoadMoreAdapter
import kotlinx.android.synthetic.main.activity_user_select.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class OrganizationDeptSelectAdapter(val multipleSelect:Boolean, val list:ArrayList<ContactsResult.UserItem>,val contactType:Int): LoadMoreAdapter<ContactsResult.DeptItem>() {
    override fun onCreateLoadMoreViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_organization_dept_select, parent, false)
        return ViewHolder(view)
    }

    override fun onLoadMoreBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bindData(mData[position])
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindData(model: ContactsResult.DeptItem) {
            mView.onClick {
                model.id?.let {
                    OrganizationDeptUserSelectActivity.toActivity(mView.context,it,model.name,list,multipleSelect,contactType,OrganizationSelectActivity.REQUEST_CODE)
                }

            }
            model.name?.let {
                mView.kvl_info.setDest(it)
            }?:mView.kvl_info.setDest("")
        }
    }
}