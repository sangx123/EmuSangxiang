package com.xiang.one.demo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.xiang.one.BaseActivity
import com.xiang.one.R
import com.xiang.one.utils.viewpager.MFragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_tab_layout_view_pager.*

class TabLayoutViewPagerActivity : BaseActivity() {
    var contactType=0
    var multipleSelect=true
    val tabTitles= arrayOf("部门人员", "店长/门店","test1","test2")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout_view_pager)

        //var mAdapter= MFragmentStatePagerAdapter(supportFragmentManager, arrayOf(OrganizationDeptSelectFragment.newInstance(ArrayList(), multipleSelect, contactType), OrganizationShopSelectFragment.newInstance(ArrayList(), multipleSelect, contactType),Test1Fragment(),Test2Fragment()).toMutableList() as List<Fragment>?, tabTitles)

        //mViewPager.adapter=mAdapter
        mViewPager.setPageTransformer(false, ViewPager.PageTransformer { page, position ->
            when {
                position > 1 -> page.alpha = 1f
                position >= 0 -> page.alpha = 1 - position
                position > -1 -> page.alpha = 1 + position
                else -> page.alpha = 0f
            }
        })
        mTabLayout.setupWithViewPager(mViewPager)
    }
}
