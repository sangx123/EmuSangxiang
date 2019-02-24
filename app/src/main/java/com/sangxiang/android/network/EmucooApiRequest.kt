package com.sangxiang.android.network

import android.widget.Toast
import com.google.gson.*
import com.sangxiang.android.App
import com.sangxiang.android.BuildConfig
import com.sangxiang.android.network.param.SubmitableClass
import com.sangxiang.android.network.param.asSubmitableClass
import com.google.gson.reflect.TypeToken
import com.orhanobut.hawk.Hawk
import com.sangxiang.android.utils.SharePerferenceConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.error
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson




/**
 * 10/03/2018  6:00 PM
 * Created by Zhang.
 */

//class EnvelopConverterFactory : Converter.Factory() {
//    val TAG = "EnvelopConverterFactory"
//    //    val gson = Gson()
//    //从返回数据里面取出 需要的data的节点
//    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>,
//                                       retrofit: Retrofit): Converter<ResponseBody, *>? {
//        val realType = TypeToken.getParameterized(EmucooEnvelopModel::class.java, type).type
//        val delegate: Converter<ResponseBody, EmucooEnvelopModel<*>> =
//                retrofit.nextResponseBodyConverter<EmucooEnvelopModel<*>>(this, realType, annotations)
//        return ResponseConverter(delegate, type)
//    }
//
//    inner class ResponseConverter(val delegate: Converter<ResponseBody, EmucooEnvelopModel<*>>, val type: Type) : Converter<ResponseBody, Any> {
//        override fun convert(value: ResponseBody): Any? {
//            val m: EmucooEnvelopModel<*> = delegate.convert(value)
//
//
//            if (m.respCode == "401") {
//                MainHandler.post({
//                    Toast.makeText(App.mInstance, m.respMsg, Toast.LENGTH_SHORT).show();
//                }, 0)
//                //EventBus.getDefault().post(SyncStateContract.Constants.EVENT_BUS_LOG_OUT to "OK")
//                //token失败
//                return null
//            }
//
//            if (m.respCode != "000") {
//                MainHandler.post({
//                    Toast.makeText(App.mInstance, m.respMsg, Toast.LENGTH_SHORT).show();
//                }, 0)
//                return null
//            }
//
//
//
//            if (type == object : TypeToken<String>() {}.type && m.data == null) {
//                return "RESPONSE_OK"
//            }
//
//            val data = m.data
//            return data
//        }
//    }

//    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit): Converter<*, RequestBody>? {
//
//        val realType = TypeToken.getParameterized(SubmitableClass::class.java, type).type
////        val submitType = object :TypeToken<SubmitableClass<Any>>(){}.type
//        val converter: Converter<Any, RequestBody> = retrofit.nextRequestBodyConverter<Any>(this, realType, parameterAnnotations, methodAnnotations)
//        return RequestConverter(converter)
//    }
//
//    inner class RequestConverter(val delegate: Converter<Any, RequestBody>) : Converter<Any, RequestBody>,AnkoLogger {
//        override fun convert(value: Any): RequestBody {
//            error("convert:${value.toString()}")
//            val requestBody: RequestBody = delegate.convert(value.asSubmitableClass<Any>())
//            return requestBody
//        }
//    }
//}

//加请求头
class AddHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (!Hawk.get<String>(SharePerferenceConfig.userToken).isNullOrBlank()) {
            builder.addHeader("userToken", Hawk.get<String>(SharePerferenceConfig.userToken))
        }
        //@Headers("Version:1", "ApiType:Android")
        builder.addHeader("Version", "1")
        builder.addHeader("ApiType", "Android")
        return chain.proceed(builder.build())
    }
}

class EmucooApiRequest private constructor() {

    private val DEFAULT_TIMEOUT = 60
    lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService
    private var httpClientBuilder: OkHttpClient.Builder
    private lateinit var mReqLogFp: File
    var mCurDateTime: String? = null //yyyy-MM-dd HH:mm:ss
    //构造方法私有
    init {
        //手动创建一个OkHttpClient并设置超时时间
        httpClientBuilder = OkHttpClient.Builder()
        mReqLogFp = App.requestLog

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {

            if (BuildConfig.DEBUG) {
                mReqLogFp.appendText("------------------------------------------------\n")
                mReqLogFp.appendText(it)
                mReqLogFp.appendText("------------------------------------------------\n\n\n\n\n")
                println(it.plus("\n\n"))

                if (it.startsWith("Date") && it.endsWith("GMT")) {
                    //Sun, 01 Jul 2018 04:02:40 GMT"
                    try {
                        val date: Date = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US).parse(it.subSequence(5, it.length).toString().trim())
                        val formatedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
                        mCurDateTime = formatedDate
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
//                    println(formatedDate.plus("\n\n"))
                }
            }
        })

        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClientBuilder.addInterceptor(AddHeaderInterceptor())
        httpClientBuilder.addInterceptor(logging)
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        createApiService()
    }

    fun createApiService() {
//        此方法可以设置成Date类型
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        retrofit = Retrofit.Builder()
//                .addConverterFactory(ShowContentConverter())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClientBuilder.build())
                .build()
        apiService = retrofit.create(ApiService::class.java)

    }

    //在访问EmucooRequest时创建单例
    private object SingletonHolder {
        internal val INSTANCE = EmucooApiRequest()
    }

    //获取单例
    companion object {
        var defaultIp: String = ""
        get() {
            return BuildConfig.HOST_URL
        }
        var BASE_URL = defaultIp
            set(value) {
                if (value.isBlank()) {
                    return
                }

                val lastIpFile = App.lastIp
                if (value.toLowerCase().startsWith("http")) {
                    field = value
                    lastIpFile.writeText(field)
                    getInstance().createApiService()
                } else {
                    val pair = value.toIPPair()
                    val ip = pair.first.split(".")
                    if (ip.size == 4) {
                        for (segment in ip) {
                            val parsedSegment = segment.toIntOrNull()
                            if (parsedSegment == null) {
                                return
                            }
                            if (parsedSegment > 255) {
                                return
                            }
                        }
                        field = "http://${pair.first}:${pair.second}/"
                        lastIpFile.writeText(field)
                        getInstance().createApiService()
                    }
                }

            }
            get() {


                if (BuildConfig.DEBUG) {
                    val lastIpFile = App.lastIp
                    if (lastIpFile.exists()) {
                        val ip = lastIpFile.readText().trim()
                        if (ip.isNotBlank()) {
                            return ip
                        }
                    }
                }

                return defaultIp
            }

        private fun getInstance(): EmucooApiRequest {
            return SingletonHolder.INSTANCE
        }

        @JvmStatic
        fun getApiService(): ApiService {
            return getInstance().apiService
        }
        fun String.toIPPair():Pair<String/*ip*/,String/*port*/>{
            var tempUrl :String
            if(this.toLowerCase().startsWith("http")){
                tempUrl = this
            }else{
                tempUrl = "http://$this"
            }
            val url = URL(tempUrl)
            return url.host to if(url.port == -1) "80" else url.port.toString()
        }

    }

}