package com.sangxiang.android.ui.taobao

import io.objectbox.annotation.Id
import io.objectbox.annotation.BaseEntity
import java.util.*


data class TaobaoTask(
    var id: Int? = null,
    var userid: Int? = null,
    var createTime: Date? = null,
    var sousuoguanjianzi: String? = null,
    var zhangguiming: String? = null,
    var shangpinlianjie: String? = null,
    var shangpinjiage: Float? = null,

    var shuadanshuliang: Int? = null,

    var shuadanyongjin: Float? = null,

    var renqunbiaoqian: String? = null,

    var jialiao: Boolean? = null,

    var liulanqita: Boolean? = null,

    var tingliushichang: Boolean? = null,

    var daituhaoping: Boolean? = null,

    var huobisanjia: Boolean? = null,

    var zhenshiqianshou: Boolean? = null,

    var totalPrice: Float? = null,

    var status: Boolean? = null
)

class TaoBaoHomeTaskParam (
    var pageSize: Int = 10,
    var pageNumber: Int = 1,
    var state: Int? = 1
)