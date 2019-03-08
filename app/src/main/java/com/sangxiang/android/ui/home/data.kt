package com.sangxiang.android.ui.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BannerBean : Serializable {
    //是否需要显示
    var disabled: String? = null
    //显示顺序
    var displayOrder: String? = null
    //  活动开始时间
    var startTime: String? = null
    //  活动结束时间
    var endTime: String? = null
    //  图片的地址
    var imageUrl: String = ""
    //  广告类型，1是图片点击可以跳转到指定Url，2，是图片点击不能跳转，其实是App内的跳转逻辑
    var type: ActionTypeEnum = ActionTypeEnum.NoneLink
    //  跳转地址
    var url: String? = null
    //  这条广告的Id
    var id: Int = 0

}

enum class ActionTypeEnum private constructor() {
    URL,
    NoneLink
}