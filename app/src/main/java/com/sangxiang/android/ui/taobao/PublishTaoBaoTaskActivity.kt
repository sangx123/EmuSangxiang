package com.sangxiang.android.ui.taobao

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R
import com.sangxiang.android.network.EmucooApiRequest
import com.sangxiang.android.network.model.BaseResult
import com.sangxiang.android.utils.RegexUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_publish_tao_bao_task.*
import kotlinx.android.synthetic.main.layout_key_value_item.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.regex.Matcher
import java.util.regex.Pattern

class PublishTaoBaoTaskActivity : BaseActivity() {
    var jialiao:Boolean=false
    var liulanqita:Boolean=false
    var tingliushichang:Boolean=false
    var daituhaoping:Boolean=false
    var huobisanjia:Boolean=false
    var zhenshiqianshou:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_tao_bao_task)
        initView()
    }

    private fun initView() {
        kv_jialiao.onClick {
            jialiao=!jialiao
            kv_jialiao.setLeftIcon(jialiao)
        }

        kv_liulanqitashangpin.onClick {
            liulanqita=!liulanqita
            kv_liulanqitashangpin.setLeftIcon(liulanqita)
        }

        kv_tingliushijian.onClick {
            tingliushichang=!tingliushichang
            kv_tingliushijian.setLeftIcon(tingliushichang)
        }

        kv_daituhaoping.onClick {
            daituhaoping=!daituhaoping
            kv_daituhaoping.setLeftIcon(daituhaoping)
        }

        kv_huobisanjia.onClick {
            huobisanjia=!huobisanjia
            kv_huobisanjia.setLeftIcon(huobisanjia)
        }

        kv_zhenshiqianshou.onClick {
            zhenshiqianshou=!zhenshiqianshou
            kv_zhenshiqianshou.setLeftIcon(zhenshiqianshou)
        }

        btn_submit.onClick {
            var model=TaobaoTask()
            model.jialiao=jialiao
            model.liulanqita=liulanqita
            model.tingliushichang=tingliushichang
            model.daituhaoping=daituhaoping
            model.huobisanjia=huobisanjia
            model.zhenshiqianshou=zhenshiqianshou
            if(kv_sousuoguanjianzi.edit_content.text.toString().isNullOrBlank()){
                toast("请设置淘宝搜索关键字!")
                return@onClick
            }
            if(kv_zhangguiming.edit_content.text.toString().isNullOrBlank()){
                toast("请设置淘宝掌柜名!")
                return@onClick
            }
            if(kv_shangpinlianjie.edit_content.text.toString().isNullOrBlank()){
                toast("请设置商品链接!")
                return@onClick
            }
            if(!RegexUtils.isNumber(kv_shangpinjiage.edit_content.text.toString().trim())){
                toast("请设置商品价格!且必须是正数")
                return@onClick
            }
            if(!isInteger(kv_shuadanshuliang.edit_content.text.toString().trim())){
                toast("请设置刷单数量,且必须是整数!")
                return@onClick
            }


            model.sousuoguanjianzi=kv_sousuoguanjianzi.edit_content.text.toString()
            model.zhangguiming=kv_zhangguiming.edit_content.text.toString()
            model.shangpinlianjie=kv_shangpinlianjie.edit_content.text.toString()
            model.shangpinjiage=kv_shangpinjiage.edit_content.text.toString().trim().toFloat()
            model.shuadanshuliang=kv_shuadanshuliang.edit_content.text.toString().trim().toInt()
            model.shuadanyongjin=kv_shuadanyongjin.edit_content.text.toString().trim().toFloat()
            model.renqunbiaoqian=kv_renqunbiaoqian.edit_content.text.toString()

            EmucooApiRequest.getApiService().publishTaoBaoTask(model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResult<String>> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                            mDisposables.add(d)
                        }

                        override fun onNext(t: BaseResult<String>) {
                            toast("创建成功")
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }

                    })
        }
    }

    /**方法二：推荐，速度最快
      * 判断是否为整数
      * @param str 传入的字符串
      * @return 是整数返回true,否则返回false
    */

    fun isInteger(str: String): Boolean {
        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
        return pattern.matcher(str).matches()
    }


    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    fun isNumeric(str: String): Boolean {
        var pattern = Pattern.compile("-?(0|([1-9]\\d*))\\.\\d+")
        var isNum = pattern.matcher(str)
        if (!isNum.matches()) {
            return false
        }
        return true
    }

}
