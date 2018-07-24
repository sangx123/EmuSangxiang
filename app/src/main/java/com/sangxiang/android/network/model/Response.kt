package com.sangxiang.android.network.model

import com.sangxiang.android.App
import com.google.gson.annotations.SerializedName
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

data class UserModel(
        @SerializedName("userToken") val userToken: String = "",
        @SerializedName("headImgUrl") var headImgUrl: String = "",
        @SerializedName("userName") val userName: String = "",
        @SerializedName("userType") val userType: String= "",
        @SerializedName("departmentType") val departmentType: Float= 0f,
        @SerializedName("departmentID") val departmentID: Float= 0f,
        @SerializedName("departmentName") val departmentName: String= "",
        @SerializedName("shopNameArr") var shopNameArr: List<ShopNameArr> = listOf<ShopNameArr>(),
        val userId :Int = 0,
        val usePlan:Boolean = true,
        @SerializedName("isShopManager") val isShopManager: Boolean= false

){
    var id:Long = 0

    fun asDBModel():UserModelDB{
        val db = UserModelDB(userToken,headImgUrl,userName,userType,departmentType,departmentID,departmentName,userId,usePlan,isShopManager)

        shopNameArr?.forEach {
            db.shopNameArr.add(it)
        }
        db.id = id
        return db
    }
}

@Entity
data class UserModelDB(
        @SerializedName("userToken") val userToken: String = "",
        @SerializedName("headImgUrl") val headImgUrl: String = "",
        @SerializedName("userName") val userName: String = "",
        @SerializedName("userType") val userType: String= "",
        @SerializedName("departmentType") val departmentType: Float= 0f,
        @SerializedName("departmentID") val departmentID: Float= 0f,
        @SerializedName("departmentName") val departmentName: String= "",
        val userId :Int = 0,
        val usePlan:Boolean = true,
        @SerializedName("isShopManager") val isShopManager: Boolean= false

){
    @Id
    var id:Long = 0
    @SerializedName("shopNameArr") lateinit var shopNameArr: ToMany<ShopNameArr>


    fun asUserModel():UserModel{
        val list = mutableListOf<ShopNameArr>()
        shopNameArr?.forEach {
            list.add(it)
        }
        val model = UserModel(userToken,headImgUrl,userName,userType,departmentType,departmentID,departmentName,list,userId,usePlan,isShopManager)
        model.id = id
        return model
    }

    fun saveToDb():Long{
        return App.mBoxStore.boxFor(UserModelDB::class.java).put(this)
    }

    fun deleteInDb(){
        App.mBoxStore.boxFor(UserModelDB::class.java).remove(id)
    }
}

@Entity
data class ShopNameArr(
        @SerializedName("shopName") val shopName: String = "",
        @SerializedName("shopID") val shopID: Int = 0
){
    @Id
    var id:Long=0

    lateinit var userModel: ToOne<UserModelDB>
}
data class EmucooEnvelopModel<T>(
        @SerializedName("version") val version: Int = 0,
        @SerializedName("cmd") val cmd: String = "",
        @SerializedName("pageType") val pageType: String = "",
        @SerializedName("respCode") val respCode: String = "",
        @SerializedName("respMsg") val respMsg: String = "",
//		@SerializedName("data") var data: JsonObject?= null ,
        @SerializedName("data") var data: T? = null,
        @SerializedName("pageSize") val pageSize: Int = 0,
        @SerializedName("pageNumber") val pageNumber: Int = 0,
        @SerializedName("sign") val sign: String = ""
)

