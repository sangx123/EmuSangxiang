package com.emucoo.business_manager.network.model

import java.io.Serializable

class ContactsResult:Serializable {

    /**
     * type=1： 按照用户名搜素，返回结果只有users
     * type=2： 按照部门搜素，返回结果有users或者depts，或者两者都有
     * type=3： 按照店铺搜素，返回值只有areas
     * type=4： 按照店铺搜素，返回值只有shops
     */
     val type: Int = 0
     var users: List<UserItem>? = null
     var depts: List<DeptItem>? = null
     var areas: List<AreaItem>? = null
     var shops: List<ShopItem>? = null

    class UserItem (
         val id: Long = 0,
         val name: String? = null,
         val imgUrl: String? = null,
         var selected:Boolean=false
    ):Serializable

    class DeptItem :Serializable{
         val id: Long = 0
         val name: String? = null
    }

    class AreaItem :Serializable {
         val areaId: Long = 0
         val brandId: Long = 0
         val areaName: String? = null
         val brandName: String? = null
         val title: String? = null
    }

    class ShopItem:Serializable {
         val shopId: Long = 0
         val shopName: String? = null
         val managerId: Long = 0
         val managerName: String? = null
         val managerImgUrl: String? = null
         var selected:Boolean=false
    }
}