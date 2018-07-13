package com.emucoo.business_manager.work

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.utils.BitmapUtils.Companion.loadImageOrUserNameOnIV
import com.emucoo.business_manager.widgets.LoadMoreAdapter
import kotlinx.android.synthetic.main.item_user_select.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class UserSelectAdapter: LoadMoreAdapter<ContactsResult.UserItem>() {
    var list:ArrayList<ContactsResult.UserItem>?=null
    override fun onCreateLoadMoreViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_select, parent, false)
        return ViewHolder(view)
    }

    override fun onLoadMoreBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bindData(mData[position])
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindData(model: ContactsResult.UserItem) {
            mView.mDelete.onClick {
                   list?.let {
                       for (i in it.indices){
                           if(it[i].id==model.id){
                               it.removeAt(i)
                               break
                           }
                       }
                   }
                   setData(list,false)
            }
            mView.mName.text=""
            model.name?.let {
                mView.mName.text= model.name
                loadImageOrUserNameOnIV(model.imgUrl, model.name, mView.mImage)
            }
        }
    }

    fun setModel(list:ArrayList<ContactsResult.UserItem>?){
        this.list=list
        if(this.list==null){
            this.list= ArrayList()
        }
        setData(this.list,false)
    }
}

