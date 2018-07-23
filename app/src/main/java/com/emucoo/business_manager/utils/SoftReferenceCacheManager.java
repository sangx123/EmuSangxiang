package com.emucoo.business_manager.utils;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class SoftReferenceCacheManager {
    private static SoftReferenceCacheManager softReferenceCacheManager;
    private final Map<String, SoftReference<Bitmap>> map;
    // 单例模式
    private SoftReferenceCacheManager() {
        map = new HashMap<String, SoftReference<Bitmap>>();
    }
    // 初始化对象的操作
    public static SoftReferenceCacheManager getSoftReferenceCacheManager() {
        if (softReferenceCacheManager == null) {
            softReferenceCacheManager = new SoftReferenceCacheManager();
        }
        return softReferenceCacheManager;
    }
    // 将图片添加到软引用中
    public void put(String url, Bitmap bm) {
        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bm);
        map.put(url, soft);
    }
    // 从软引用中取出图片
    public Bitmap get(String url) {
        Bitmap bm = null;
        SoftReference<Bitmap> soft = map.get(url);
        if (soft != null) {
            bm = soft.get();
        }
        return bm;
    }
    // 将图片在软引用中删除
    public void remove(String url) {
        map.remove(url);
    }
}