package com.example.sangxiang.network
import com.example.sangxiang.network.model.UserModel
import com.example.sangxiang.network.param.LoginSubmit
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
}

