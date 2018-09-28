package com.xd.demi

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.exyui.android.debugbottle.components.DTInstaller
import com.orhanobut.hawk.Hawk
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.xd.demi.network.RetrofitManager
import com.xd.demi.todo.BuildConfig
import com.xd.demi.todo.R
import com.xd.demi.utils.AppBlockCanaryContext
import com.xd.demi.utils.ContentInjector
import kotlin.properties.Delegates


/**
 * Created by demi on 2018/8/11 下午3:23.
 */
class TDLApplication : Application() {
    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, _ ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
        }
    }
    companion object {
        var context: Context by Delegates.notNull()
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        Utils.init(this)
        LogUtils.Builder().setBorderSwitch(false)
        Hawk.init(this).build()
        initDeBugBottle()
    }

    private fun initDeBugBottle() {
        if (BuildConfig.DEBUG) {
            getSharedPreferences("dt_settings", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("BOTTLE_ENABLE", true)
                    .putBoolean("NETWORK_SNIFF", true)
                    .putBoolean("BLOCK_CANARY_ENABLE", true)
                    .putBoolean("LEAK_CANARY_ENABLE", true)
                    .apply()
            DTInstaller.install(this)
                    .setBlockCanary(AppBlockCanaryContext(this))
                    .setOkHttpClient(RetrofitManager.client)
                    .setInjector(ContentInjector())
                    .enable()
                    .run()
        }
    }

}