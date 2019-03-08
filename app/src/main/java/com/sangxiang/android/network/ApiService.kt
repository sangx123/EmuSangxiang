package com.sangxiang.android.network
import com.sangxiang.android.network.param.ContactsParam
import com.sangxiang.android.network.param.LoginSubmit
import com.google.gson.annotations.SerializedName
import com.sangxiang.android.network.model.*
import com.sangxiang.android.network.param.ParamCommentSelectIn
import com.sangxiang.android.ui.login.RegisterParam
import com.sangxiang.android.ui.taobao.TaoBaoHomeTaskParam
import com.sangxiang.android.ui.taobao.TaobaoTask
import com.sangxiang.android.utils.appUpdate.VersionModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * 10/03/2018  5:52 PM
 * Created by Zhang.
 */
interface ApiService {
    @InterfaceUseCase("登录")
    @POST("/api/index/login")
    fun login(@Body loginSubmit: LoginSubmit): Observable<BaseResult<UserModel>>

    @InterfaceUseCase("注册")
    @POST("/api/index/register")
    fun register(@Body model: RegisterParam): Observable<BaseResult<UserModel>>

    @InterfaceUseCase("重置密码")
    @POST("/api/index/resetPassword")
    fun resetPassword(@Body model: RegisterParam): Observable<BaseResult<String>>


    @InterfaceUseCase("按照部门查询")
    @POST("/api/contacts/search/bydept")
    fun searchBydept(@Body model: ContactsParam): Observable<BaseResult<ContactsResult>>

    @InterfaceUseCase("按照门店查询")
    @POST("/api/contacts/search/byshop")
    fun searchByshop(@Body model: ContactsParam): Observable<BaseResult<ContactsResult>>

    @InterfaceUseCase("按照名称关键词查询")
    @POST("/api/contacts/search/byname")
    fun searchByname(@Body model: ContactsParam): Observable<BaseResult<ContactsResult>>

    @InterfaceUseCase("版本管理")
    @POST("/api/version/checkUpdate")
    fun checkUpdate(@Body request: Map<String, String>): Observable<BaseResult<String>>


    @InterfaceUseCase("查询评论数量")
    @POST("/api/comment/getCommentNum")
    fun getCommentNum(@Body model: ParamCommentSelectIn): Observable<BaseResult<CommentNum>>

    @InterfaceUseCase("发布淘宝任务")
    @POST("/api/taobao/publishTaoBaoTask")
    fun publishTaoBaoTask(@Body model: TaobaoTask): Observable<BaseResult<String>>

    @InterfaceUseCase("淘宝任务大厅")
    @POST("/api/taobao/getTaoBaoHomeTask")
    fun getTaoBaoHomeTask(@Body model: TaoBaoHomeTaskParam): Observable<BaseResult<List<TaobaoTask>>>

}

