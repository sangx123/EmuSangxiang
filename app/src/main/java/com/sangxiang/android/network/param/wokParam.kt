package com.sangxiang.android.network.param

import java.io.Serializable

class ContactsParam (
     val keyword: String? = null,
     val deptId: Long? = null,
     val areaId: Long? = null,
     val brandId: Long? = null,
     val contactType:Int=0
)

//@ApiModel(value="查询评论/回复")
class ParamCommentSelectIn
//
//        when(workType){
//
//            1,2,3,5->{
//                //1：常规任务，2：指派任务：3：改善任务，5：工作备忘（workID 和 subID必须）

//
//            }
//            4->{
//                //巡店任务(reportID必须)
//
//            }
//            6->{
//                //评论（commentID必须）
//
//            }
//            7->{
//                //维修任务（repairID必须）
//
//            }
//        }

//1：常规任务，2：指派任务：3：改善任务，4：巡店任务，5：工作备忘，6：评论，7：维修任务
//@ApiModelProperty(value="评论类型",name="workType",required = true,notes = "1：常规任务，2：指派任务：3：改善任务，4：巡店任务，5：工作备忘，6：评论，7：维修任务")
(
        var workType: Int = 0,
        var workID: String = "",
        var subID: String = "",
        //@ApiModelProperty(value="维修任务Id",name="repairID",notes = "workType=7 维修任务评论传此参数")
        var repairID: Long = 0,
        //@ApiModelProperty(value="报告Id",name="reportID",notes = "workType=4 巡店任务评论传此参数")
        //如果 workType=4 需传reportID
        var reportID: Long = 0,
        //@ApiModelProperty(value="评论ID",name="commentID",notes = "workType=6 查询回复传此参数")
        var commentID: Long = 0
): Serializable

