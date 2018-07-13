package com.emucoo.business_manager.utils;

import android.app.Activity;
import android.widget.ImageView;
import com.emucoo.business_manager.R;
import com.lzy.imagepicker.loader.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader implements ImageLoader {
    private static final String TAG = PicassoImageLoader.class.getSimpleName();

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

        if(path==null){
            return;
        }

        if(!path.toLowerCase().startsWith("http")){
            path = "file://"+path;
        }

        Picasso.with()
                .load(path)//
                .noPlaceholder()
//                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }

    @Override
    public void displayImage(Activity activity, int resource, ImageView imageView, int width, int height) {
        Picasso.with()
                .load(resource)//
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);

    }

    @Override
    public void clearMemoryCache() {
    }
}