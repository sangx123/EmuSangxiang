package com.sangxiang.android.designpattern

import android.app.Activity
import android.os.Bundle
import com.sangxiang.android.BaseActivity
import com.sangxiang.android.R

//工厂设计模式
class FactoryActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_factory)
        var baseDao=Factory.createMySql()
    }

    interface BaseDao{
        fun getModel()
        fun getList()
    }

    class MySql:BaseDao {
        override fun getList() {

        }

        override fun getModel(){

        }
    }

    class SqlServer:BaseDao{
        override fun getModel() {

        }

        override fun getList() {

        }

    }

    class Factory{
        companion object {
            fun createMySql():MySql{
                return MySql()
            }

            fun createSqlServer():SqlServer{
                return SqlServer()
            }
        }
    }

}
