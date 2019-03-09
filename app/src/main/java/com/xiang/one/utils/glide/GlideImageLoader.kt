package com.xiang.one.utils.glide

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.lzy.imagepicker.loader.ImageLoader

class GlideImageLoader : ImageLoader{
    override fun displayImage(activity: Activity?, path: String?, imageView: ImageView?, width: Int, height: Int) {
        activity?.let {
            Glide.with(it).load(path).into(imageView!!)
        }

    }

    override fun displayImage(activity: Activity?, resource: Int, imageView: ImageView?, width: Int, height: Int) {
        activity?.let {
            Glide.with(activity).load(resource).into(imageView!!)
        }

    }

    override fun clearMemoryCache() {

    }

}