package com.emucoo.business_manager.network
import com.emucoo.business_manager.network.model.ContactsResult
import com.emucoo.business_manager.network.model.UserModel
import com.emucoo.business_manager.network.param.ContactsParam
import com.emucoo.business_manager.network.param.LoginSubmit
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


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
}

