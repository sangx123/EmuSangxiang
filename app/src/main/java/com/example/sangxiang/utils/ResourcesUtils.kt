package com.example.sangxiang.utils

import android.support.v4.content.ContextCompat
import com.example.sangxiang.App

class ResourcesUtils{
    companion object {
       fun getColor(id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }
    }
}
