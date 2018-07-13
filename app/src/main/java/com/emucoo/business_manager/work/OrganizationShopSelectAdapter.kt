package com.emucoo.business_manager.work

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.widgets.LoadMoreAdapter
import kotlinx.android.synthetic.main.item_organization_shop_select.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class OrganizationShopSelectAdapter(val multipleSelect:Boolean, val list:ArrayList<ContactsResult.UserItem> ,val contactType:Int): LoadMoreAdapter<ContactsResult.AreaItem>() {
    override fun onCreateLoadMoreViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_organization_shop_select, parent, false)
        return ViewHolder(view)
    }

    override fun onLoadMoreBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bindData(mData[position])
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindData(model: ContactsResult.AreaItem) {

            mView.onClick {
                OrganizationShopUserSelectActivity.toActivity(mView.context,model.brandId,model.areaId,model.brandName,list,multipleSelect,contactType,OrganizationSelectActivity.REQUEST_CODE)
            }
            model.title?.let {
                mView.kvl_info.setDest(it)
            } ?: mView.kvl_info.setDest("")
        }
    }
}