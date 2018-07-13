package com.emucoo.business_manager.work

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.utils.BitmapUtils.Companion.loadImageOrUserNameOnIV
import com.emucoo.business_manager.utils.Utils
import com.emucoo.business_manager.widgets.LoadMoreAdapter
import kotlinx.android.synthetic.main.item_organization_dept_user_select.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class UserSearchAdapter(val multipleSelect:Boolean,var list:ArrayList<ContactsResult.UserItem>): LoadMoreAdapter<ContactsResult.UserItem>() {
    override fun onCreateLoadMoreViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_organization_dept_user_select, parent, false)
        return ViewHolder(view)
    }

    override fun onLoadMoreBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bindData(mData[position])
        }
    }
    interface CallListenter{
        fun onClick(num:Int)
    }
    var mCallListenter:CallListenter?=null
    fun  setCallListenter(callListenter:CallListenter?) {
        mCallListenter=callListenter
    }
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindData(model: ContactsResult.UserItem) {
            mView.onClick {
                if(!multipleSelect) {
                    mData.forEach {
                        it.selected = false
                    }
                }
                model.selected = !model.selected
                notifyDataSetChanged()
                //设置同步选中数据
                if(model.selected){
                    //如果是选中状态
                    if(!multipleSelect){
                        //如果是单选的话,只保留一个
                        list.clear()
                        list.add(model)
                    }else{
                        //如果是多选的话，新的选中就添加，
                        list.add(model)
                    }

                }else{
                    //如果是取消选中的话
                    if(!multipleSelect){
                        //如果是单选的话，全部清除
                        list.clear()
                    }else{
                        //如果是多选的话，取消选中的话，从已经存在里面的移出
                        for(i in list.indices){
                            if(list[i].id==model.id){
                                list.removeAt(i)
                                break
                            }
                        }
                    }
                }

                mCallListenter?.let {
                    it.onClick(mData.filter { it.selected }.size)
                }
            }
            mView.mName.text=""
            model.name?.let {
                mView.mName.text=it
                loadImageOrUserNameOnIV(model.imgUrl,it,mView.mImage)
            }?: Utils.loadHeadPicToVh(model.imgUrl,mView.mImage)


            if(model.selected){
                mView.iv.visibility=View.VISIBLE
            }else{
                mView.iv.visibility=View.GONE
            }
        }
    }


}