package com.sangxiang.android.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class LruCacheManager {
    private static LruCacheManager lruCacheManager;
    private SoftReferenceCacheManager softReferenceCacheManager;
    private LruCache<String, Bitmap> lruCache;

    // 配置一级缓存设置LruCache
    private LruCacheManager() {
        // (Runtime.getRuntime().maxMemory()运行时系统所分配的内存资源
        Log.e("sangxiang", "分配的lru内存为："+(int) (Runtime.getRuntime()
                .maxMemory() / 8));
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime()
                .maxMemory() / 8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (value != null) {
                    return value.getRowBytes() * value.getHeight();
                }
                return 0;
            }

            // 当内存资源不足是进行调用
            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        Bitmap oldValue, Bitmap newValue) {
                Log.e("sangxiang", "entryRemoved:内存不足 " );
                if (evicted) {
                    // 放到软引用当中
                    softReferenceCacheManager.put(key, oldValue);
                    Log.e("sangxiang", "将文件放入软引用中" );
                }
            }
        };
        // 初始化softRefrenceCacheManager
        softReferenceCacheManager = SoftReferenceCacheManager
                .getSoftReferenceCacheManager();
    }

    // 初始化LruCachaManager操作
    public static LruCacheManager getLruCacheManager() {
        if (lruCacheManager == null) {
            lruCacheManager = new LruCacheManager();
        }
        return lruCacheManager;
    }

    // 将图片添加到lrucache中
    public void putBitmap(String url, Bitmap bm) {
        lruCache.put(url, bm);
    }

    // 从lrucache中取出图片
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        bm = lruCache.get(url);
        if (bm == null) {
            // 软引用当中取
            bm = softReferenceCacheManager.get(url);
            // 将从软引用中取出的图片添加到lrucache中,并将软引用中的删除掉
            if (bm != null) {
                lruCache.put(url, bm);
                softReferenceCacheManager.remove(url);
            }
        }
        return bm;
    }
}