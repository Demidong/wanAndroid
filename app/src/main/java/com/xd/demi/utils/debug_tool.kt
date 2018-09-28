package com.xd.demi.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import com.exyui.android.debugbottle.components.injector.Injector
import com.exyui.android.debugbottle.ui.BlockCanaryContext
import com.xd.demi.TDLApplication
import com.xd.demi.todo.BuildConfig
import java.io.File
import java.util.*

class AppBlockCanaryContext(tdlApplication: TDLApplication) : BlockCanaryContext(tdlApplication) {
    val TAG = "AppBlockCanaryContext"

    /**
     * 标示符，可以唯一标示该安装版本号，如版本+渠道名+编译平台

     * @return apk唯一标示符
     */
    val appContext = tdlApplication.applicationContext
    override val qualifier: String
        get() {
            var qualifier = ""
            try {
                val info = appContext.packageManager?.getPackageInfo(appContext.packageName, 0)
                qualifier += "${info?.versionCode}_${info?.versionName}_YYB"
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "getQualifier exception", e)
            }

            return qualifier
        }

    override val uid: String = "87224330"

    override val networkType: String = "wifi"

    override val configDuration: Int = 9999

    override val isNeedDisplay: Boolean = BuildConfig.DEBUG

    override val logPath: String = "/ktdebugtools/blocks"
}

class ContentInjector(private val ctx: Activity? = null) : Injector() {
    val runnableMap: LinkedHashMap<String, Runnable> = LinkedHashMap()

    init {
        runnableMap["进入设置界面"] = Runnable {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", TDLApplication.context.packageName, null)
            if (getContext() !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            getContext()?.startActivity(intent)
        }
    }

    fun getContext(): Activity? {
        return ctx ?: activity
    }

    override fun inject() {
        put(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                .setData(Uri.fromParts("package", TDLApplication.context.packageName, null)), "进入设置界面")

        for (entry in runnableMap) {
            put(entry.value, entry.key)
        }
    }

    fun isDebugMode(): Boolean {
        return BuildConfig.DEBUG || File(Environment.getExternalStorageDirectory(), "tianxi66_debug_tag").exists()
    }

}
