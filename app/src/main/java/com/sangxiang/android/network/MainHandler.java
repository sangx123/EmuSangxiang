package com.sangxiang.android.network;

import android.os.Handler;
import com.sangxiang.android.App;

/**
 * 10/08/2017  11:24 AM
 * Created by Zhang.
 */

public class MainHandler {

    public static Handler handler = new Handler(App.mInstance.getMainLooper());

    public static  void post(Runnable runnable , long delay){
        if(delay > 0 ){
            handler.postDelayed(runnable,delay);
        }else{
            handler.post(runnable);
        }
    }
}
