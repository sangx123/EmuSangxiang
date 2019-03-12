package com.xiang.one.ui.maker_task


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xiang.one.BaseFragment

import com.xiang.one.R
import com.xiang.one.ui.taobao.PublishTaoBaoTaskActivity
import kotlinx.android.synthetic.main.fragment_maker.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MakerFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        mMakeTask.onClick {
            startActivity<PublishTaoBaoTaskActivity>()
        }
    }


}
/*
暂定规则
1，一个淘宝账号2周只能刷同一个商家一次
2，一个刷手目前暂时只能绑定一个淘宝账号(一定请使用自己的淘宝账号)
3，必须先绑定账号后，才可以接受任务
4，晚上8点之后接收任务大厅关闭

不扣除金币，锁定金币
    商家（进行中的任务）（全部任务==进行细分）
    -》任务列表
    任务编号，任务时间，任务金额，数量/当前参与人数，金额，任务结算（仅第二天才可结算）
    任务详情页
    刷手    购买中，已提交，待确认收货，已提交确认收货，操作
          -      待审核     -             -         评价
                                        待审核


    刷手（进行中的任务）（全部任务==进行中的任务，已完成的任务）

    刷手    购买中，已提交，待确认收货，已提交确认收货，操作

    任务编号，任务要求，任务状态，


    （已接收任务，待审核任务，待确认收货，任务完成审核）                                                              刷手
1，商家发布任务                                                        刷手接收任务（当天晚上8点前提交购买凭据）
2，                                                                    刷手按照要求购买商品，提交购买凭据
3，商家确认是否完成任务（商家给刷手评分5星好评）可审核2次              待确认收货 （或者重新提交购买凭据）
   商家可提交违规凭据（比如，非关键字，）
4，商家确认是否确认收货                                                任务完美结束
                                                                       完成任务:锁定金钱

商家点击:任务结算（解除锁定金钱）

每一个任务详情中都有一个任务结算，只能
支付待收货的刷手的所有的本金和佣金

*/
