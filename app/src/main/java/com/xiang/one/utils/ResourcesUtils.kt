package com.xiang.one.utils

import android.support.v4.content.ContextCompat
import com.xiang.one.App

class ResourcesUtils{
    companion object {
       fun getColor(id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }
    }
}
