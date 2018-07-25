package com.sangxiang.android.network
import com.sangxiang.android.network.model.ContactsResult
import com.sangxiang.android.network.model.UserModel
import com.sangxiang.android.network.param.ContactsParam
import com.sangxiang.android.network.param.LoginSubmit
import com.google.gson.annotations.SerializedName
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
    fun login(@Body loginSubmit: LoginSubmit): Observable<UserModel>

    @InterfaceUseCase("按照部门查询")
    @POST("/api/contacts/search/bydept")
    fun searchBydept(@Body model: ContactsParam): Observable<ContactsResult>

    @InterfaceUseCase("按照门店查询")
    @POST("/api/contacts/search/byshop")
    fun searchByshop(@Body model: ContactsParam): Observable<ContactsResult>

    @InterfaceUseCase("按照名称关键词查询")
    @POST("/api/contacts/search/byname")
    fun searchByname(@Body model: ContactsParam): Observable<ContactsResult>

    @InterfaceUseCase("版本管理")
    @POST("/api/version/checkUpdate")
    fun checkUpdate(@Body request: Map<String, String>): Observable<String>
}

