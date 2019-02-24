package com.sangxiang.android.ui.my


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.hawk.Hawk
import com.sangxiang.android.App

import com.sangxiang.android.R
import com.sangxiang.android.network.Constants
import com.sangxiang.android.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_my.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class MyFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btn_login_out.onClick {
            Constants.setLoginUser(null)
            startActivity<LoginActivity>()
            Hawk.deleteAll()
            activity!!.finish()
        }
    }

}
