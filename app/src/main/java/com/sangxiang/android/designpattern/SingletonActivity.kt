package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
//单例设计模式
//[Java中单例模式和静态类的区别](https://blog.csdn.net/johnny901114/article/details/11969015)
//[单例模式使用](https://itimetraveler.github.io/2016/09/08/%E3%80%90Java%E3%80%91%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F%EF%BC%9A%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F/)
class SingletonActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleton)


    }



}

/*
双重校验锁（DCL）
public class Singleton {
	/**
     * 注意此处使用的关键字 volatile，
     * 被volatile修饰的变量的值，将不会被本地线程缓存，
     * 所有对该变量的读写都是直接操作共享内存，从而确保多个线程能正确的处理该变量。
     */
    private volatile static Singleton singleton;
    private Singleton() {
    }
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return singleton;
    }
}


静态内部类
public class Singleton {
    private Singleton(){
    }
      public static Singleton getInstance(){
        return SingletonHolder.sInstance;
    }
    private static class SingletonHolder {
        private static final Singleton sInstance = new Singleton();
    }
}
 */




