package com.emucoo.business_manager.work

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.emucoo.business_manager.R
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.utils.BitmapUtils.Companion.loadImageOrUserNameOnIV
import com.emucoo.business_manager.utils.CircleTransform
import com.emucoo.business_manager.utils.Utils
import com.emucoo.business_manager.widgets.LoadMoreAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_organization_dept_user_select.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

class OrganizationDeptUserSelectAdapter(val multipleSelect:Boolean,var list:ArrayList<ContactsResult.UserItem>): LoadMoreAdapter<ContactsResult.UserItem>() {
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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bindData(model: ContactsResult.UserItem) {
            mView.onClick {
                //设置mData的选中
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

//            if(multipleSelect){
//                //多选
//                mView.iv.visibility = View.VISIBLE
//                if(model.selected){
//                    mView.iv.imageResource = R.drawable.icon_multiselect_selected
//                }else{
//                    mView.iv.imageResource = R.drawable.icon_multiselect_unselect
//                }
//            }else {
//                //单选
//                if (model.selected) {
//                    mView.mName.textColor = 0xff22a2e4.toInt()
//                    mView.iv.visibility = View.VISIBLE
//                } else {
//                    mView.mName.textColor = 0xff202235.toInt()
//                    mView.iv.visibility = View.GONE
//                }
//            }
        }
    }


}