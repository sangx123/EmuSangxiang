package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy


/*
[Java设计模式之代理模式(Proxy)](https://blog.csdn.net/liangbinny/article/details/18656791)
 */
class ProxyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proxy)
        //静态代理调用
        val subject = ProxySubject()
        subject.request()  //代理者代替真实者做事情

        //动态代理调用
        val rs = RealSubject1()
        //以下是分解步骤
        /*
		Class c = Proxy.getProxyClass(cls.getClassLoader(), cls.getInterfaces());
		Constructor ct = c.getConstructor(new Class[]{InvocationHandler.class});
		Subject subject =(Subject) ct.newInstance(new Object[]{handler});
		*/

        //以下是一次性生成
        val subject1 = Proxy.newProxyInstance(rs.javaClass.classLoader, rs.javaClass.interfaces, DynamicSubject(rs)) as Subject1
        subject1.request()
    }

    //---------------------------------静态代理-----------------------------------------
    /**
     * 抽象角色
     */
    abstract inner class Subject {
         abstract fun request();
    }

    /**
     * 真实的角色
     */
    inner class RealSubject : Subject() {

        override fun request() {

        }

    }

    /**
     * 静态代理，对具体真实对象直接引用
     * 代理角色，代理角色需要有对真实角色的引用，
     * 代理做真实角色想做的事情
     */
    inner class ProxySubject : Subject() {

        private var realSubject: RealSubject? = null

        /**
         * 除了代理真实角色做该做的事情，代理角色也可以提供附加操作，
         * 如：preRequest()和postRequest()
         */
        override fun request() {
            preRequest()  //真实角色操作前的附加操作

            if (realSubject == null) {
                realSubject = RealSubject()
            }
            realSubject!!.request()

            postRequest()  //真实角色操作后的附加操作
        }

        /**
         * 真实角色操作前的附加操作
         */
        private fun postRequest() {
            // TODO Auto-generated method stub

        }

        /**
         * 真实角色操作后的附加操作
         */
        private fun preRequest() {
            // TODO Auto-generated method stub

        }

    }


    //--------------------------------动态代理---------------------------------

    /**
     * 抽象角色
     * 这里应改为接口
     */
    interface Subject1 {
        fun request()
    }

    /**
     * 真实的角色
     * 实现接口
     */
    inner class RealSubject1 : Subject1 {

        override fun request() {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 动态代理， 它是在运行时生成的class，在生成它时你必须提供一组interface给它， 然后该class就宣称它实现了这些interface。
     * 你当然可以把该class的实例当作这些interface中的任何一个来用。 当然啦，这个Dynamic
     * Proxy其实就是一个Proxy，它不会替你作实质性的工作， 在生成它的实例时你必须提供一个handler，由它接管实际的工作。
     */
    inner class DynamicSubject(private val sub: Any // 真实对象的引用
    ) : InvocationHandler {

        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            Log.e("DynamicSubject","before calling $method")
            method.invoke(sub, args)
            Log.e("DynamicSubject","after calling $method")
            return null
        }

    }


}
