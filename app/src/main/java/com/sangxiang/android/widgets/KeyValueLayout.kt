package com.sangxiang.android.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.sangxiang.android.App
import com.sangxiang.android.R
import com.sangxiang.android.utils.BitmapUtils.Companion.getNameBitmap
import com.sangxiang.android.utils.Utils
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.layout_key_value_item.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor
import java.util.*

class KeyValueLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private lateinit var mDesc: String
    private var mHasRightArrow = true
    private var mWaringText: String? = null
    var mResult: String? = null

    var mCacheedIvs: MutableList<ImageView> = ArrayList(4) // view 缓存
    var max_show_name = 3
    var mExecutors: ArrayList<UserModel> = ArrayList()  //如果是执行者
    set(selectedModel: ArrayList<UserModel>) {
        max_show_name = ll_executor_container.width / App.getInstance().dip(35f)
        tv_content.visibility = View.GONE
        ll_executor_container.visibility = View.VISIBLE
        tv_executor_count.visibility = View.VISIBLE


        if (selectedModel != null && selectedModel.size > 0) {
            mExecutors.clear()
            mExecutors.addAll(selectedModel)
            tv_executor_count.setText("(" + selectedModel.size + "人)")
            if (mExecutors.size > max_show_name) {
                addUserToGroup(ll_executor_container, mExecutors.subList(0, max_show_name))
            } else {
                addUserToGroup(ll_executor_container, mExecutors)
            }
        }else{
            mExecutors.clear()
            ll_executor_container.visibility = View.GONE
            tv_executor_count.visibility = View.GONE
        }
    }

    init {

        LayoutInflater.from(context).inflate(R.layout.layout_key_value_item, this, true)
        if (attrs != null) {
            val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.KeyValueLayout)
            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_text_hint)) {
                val desc = obtainStyledAttributes.getString(R.styleable.KeyValueLayout_kvl_text_hint)
                setDest(desc)
            }
            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_has_right_arrow)) {
                val has = obtainStyledAttributes.getBoolean(R.styleable.KeyValueLayout_kvl_has_right_arrow, true)
                setRightArrow(has)
            }
            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_has_left_icon)) {
                val has = obtainStyledAttributes.getBoolean(R.styleable.KeyValueLayout_kvl_has_left_icon, false)
                setTaskIcon(has)
            }
            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_edit)) {
                val has = obtainStyledAttributes.getBoolean(R.styleable.KeyValueLayout_kvl_edit, false)
                setShowEdit(has)
            }
            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_edit_hint)) {
                val str = obtainStyledAttributes.getString(R.styleable.KeyValueLayout_kvl_edit_hint)
                edit_content.hint=str
            }

            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_has_full_line)) {
                val has = obtainStyledAttributes.getBoolean(R.styleable.KeyValueLayout_kvl_has_full_line, false)
                setFullLine(has)
            }


            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_text_warning)) {
                mWaringText = obtainStyledAttributes.getString(R.styleable.KeyValueLayout_kvl_text_warning)
            }

            if (obtainStyledAttributes.hasValue(R.styleable.KeyValueLayout_kvl_right_icon)) {
                val drawable = obtainStyledAttributes.getDrawable(R.styleable.KeyValueLayout_kvl_right_icon)
                setRightIcon(drawable)

            }
            obtainStyledAttributes.recycle()
        }
    }

    private fun setShowEdit(has: Boolean) {
        if(has){
            edit_content.visibility=View.VISIBLE
            tv_content.visibility=View.GONE
        }else{
            edit_content.visibility=View.GONE
            tv_content.visibility=View.VISIBLE
        }

    }

    fun setRightIcon(drawable: Drawable?) {
        iv_arrow.setImageDrawable(drawable)

    }

    fun setRightImage(drawable: Drawable?){
        iv_right_image.visibility= View.VISIBLE
        iv_right_image.setImageDrawable(drawable)
    }

    private fun setFullLine(hasFullLine: Boolean) {
        when(hasFullLine){
            true->{
                line2.visibility=View.VISIBLE
                line1.visibility=View.GONE
            }
            false->{
                line2.visibility=View.GONE
                line1.visibility=View.VISIBLE
            }

        }

    }

    private fun setTaskIcon(hasTaskIcon: Boolean) {
        when (hasTaskIcon) {
            true -> leftIcon.visibility = View.VISIBLE
            false -> leftIcon.visibility = View.INVISIBLE
        }
    }

    public fun setRightArrow(hasRightArrow: Boolean) {
        mHasRightArrow = hasRightArrow
        when (mHasRightArrow) {
            true -> iv_arrow.visibility = View.VISIBLE
            false -> iv_arrow.visibility = View.INVISIBLE
        }
    }

    fun setDest(desc: String) {
        mDesc = desc
        tv_title.text = mDesc
    }

    public fun setHeadPic(name: String) {
        tv_content.visibility = View.INVISIBLE
    }

    public fun setResult(result: String) {

        tv_content.visibility = View.VISIBLE
        ll_executor_container.visibility = View.GONE
        tv_executor_count.visibility = View.GONE

        mResult = result
        tv_content.textColor = 0xff626471.toInt()
        tv_content.text = result
    }

    public fun setLeftIcon(show:Boolean){
        when(show){
            true -> leftIcon.visibility = View.VISIBLE
            false -> leftIcon.visibility = View.INVISIBLE
        }
    }

    /**
     * 检查result是否已经赋值了，如果没有那么显示waring
     */
    public fun pass(): Boolean {
        val pass = !mResult.isNullOrEmpty() || mExecutors.size > 0
        if (!pass) {
            mWaringText?.let {
                ll_executor_container.visibility = View.GONE
                tv_content.visibility = View.VISIBLE
                tv_content.textColor = 0xffff3333.toInt()
                tv_content.text = it
            }
        }
        return pass
    }




    private fun addUserToGroup(container: LinearLayout?, userModels: List<UserModel>?) {
        if (container == null || userModels == null || userModels.size <= 0) {
            return
        }

        val childCount = container.childCount
        val viewCount = userModels.size

        if (childCount > userModels.size) {
            //remove child to cache
            val removeChildCount = childCount - viewCount
            for (i in 0 until removeChildCount) {
                mCacheedIvs.add(container.getChildAt(0) as ImageView)
                container.removeViewAt(0)
            }
        } else if (childCount < userModels.size) {
            //get child from cache if not enough then create new child
            val addChildCount = viewCount - childCount

            val cacheSize = mCacheedIvs.size

            if (cacheSize >= addChildCount) {
                for (j in 0 until addChildCount) {
                    val iv = mCacheedIvs.removeAt(0)
                    container.addView(iv)
                }
            } else {

                for (j in 0 until cacheSize) {
                    val iv = mCacheedIvs.removeAt(0)
                    container.addView(iv)
                }

                val shouldCreate = addChildCount - cacheSize

                for (i in 0 until shouldCreate) {

                    val iv = ImageView(context)
                    val params = LinearLayout.LayoutParams(App.getInstance().dip(35f), App.getInstance().dip(35f))

                    /*
                    params.setMargins(0, 0, DipUtil.dip2px(3f), 0)
                    iv.textSize = 12f
                    iv.gravity = Gravity.CENTER
                    iv.setSingleLine()
                    iv.ellipsize = TextUtils.TruncateAt.END
                    */
                    iv.layoutParams = params
                    container.addView(iv,params)
                }
            }


        }

        for (i in 0 until viewCount) {
            val child = container.getChildAt(i) as ImageView
            /*
            child.text = userModels[i].contactsName
            child.background =
            */
            loadImageOrUserNameOnIV(userModels[i].contactsHeadUrl,userModels[i].contactsName,child)
//            child.backgroundResource = R.drawable.create_record_blue
        }
        container.requestLayout()
    }

    fun loadImageOrUserNameOnIV(headUrl:String?,userName:String, imageView:ImageView,textSize:Int = 12){
        if(headUrl.isNullOrEmpty()){
            imageView.setImageBitmap(getNameBitmap(userName,App.getInstance().dip(35f),App.getInstance().dip(35f),12f))
        }else{
            Utils.loadHeadPicToVh(headUrl,imageView)
        }
    }

    @Parcelize
    data class UserModel(
             val contactsID: Long = 0,
             val contactsName: String = "",
             val contactsHeadUrl: String = ""
    ) : Parcelable {
        var selected = false; //用于界面显示，是否被选中

        fun nextSelectedStatus() {
            selected = !selected
        }

//    class Generator : GenerateFakeData<UserModel> {
//        override fun onGenerateOne(): UserModel {
//            return UserModel(NumberGenerate.gengerateLong(), NameGenerator.getName(), HeadPicGenerator.generateNotNull())
//        }
//    }
    }
}










