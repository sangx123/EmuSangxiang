package com.example.sangxiang.work

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
        @SerializedName("contactsID") val contactsID: Long = 0,
        @SerializedName("contactsName") val contactsName: String = "",
        @SerializedName("contactsHeadUrl") val contactsHeadUrl: String = ""
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