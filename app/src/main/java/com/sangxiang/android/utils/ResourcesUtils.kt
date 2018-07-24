package com.sangxiang.android.utils

import android.support.v4.content.ContextCompat
import com.sangxiang.android.App

class ResourcesUtils{
    companion object {
       fun getColor(id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }
    }
}
