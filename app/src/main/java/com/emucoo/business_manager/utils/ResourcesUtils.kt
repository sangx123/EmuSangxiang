package com.emucoo.business_manager.utils

import android.support.v4.content.ContextCompat
import com.emucoo.business_manager.App

class ResourcesUtils{
    companion object {
       fun getColor(id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }
    }
}
