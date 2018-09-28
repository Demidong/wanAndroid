package com.xd.demi.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.orhanobut.hawk.Hawk
import com.xd.demi.commen.subscribeResp
import com.xd.demi.network.RetrofitManager
import com.xd.demi.todo.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Cookie
import java.net.URLDecoder

/**
 * Created by demi on 2018/8/13 下午2:30.
 */
class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val cookies = Hawk.get<MutableList<Cookie>>("cookies")
        var username = ""
        var password = ""
        cookies?.let {
            if (it[0].expiresAt() > System.currentTimeMillis()) {  //判断cookie是否过期，没过期直接跳转到MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            }
        }
        cookies?.forEach {
            //cookie过期了,帮助用户登录。实现免登陆
            if (it.name() == "loginUserName") {
                username = URLDecoder.decode(it.value(), "utf-8") //不解码，@符号就转义成%40了
            }
            if (it.name() == "loginUserPassword") {
                password = URLDecoder.decode(it.value(), "utf-8")
            }
            LogUtils.d("cookie expire:", "dsa" + it.expiresAt() + "cookie name:" + it.name() + "cookie value " + it.value())
        }
        if (username.isNotEmpty() && password.isNotEmpty()) {
            et_name.setText(username)
            et_pwd.setText(password)
            login(username, password)
        }
        sb_login.setOnClickListener {
            if (et_name.text.isNotEmpty() && et_pwd.text.isNotEmpty()) {
                login(et_name.text.toString(), et_pwd.text.toString())
            } else {
                ToastUtils.showLong("还没有填写账号信息")
            }
        }
    }

    private fun login(name: String, pwd: String) {
        RetrofitManager.model
                .login(name, pwd)
//                .login(User("syw120310dqp%40163.com","dong0821","","",0,0, ArrayList()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeResp(onNext = {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    LogUtils.d("登录成功----username:${it.username},id:${it.id}")
                }, onError = {
                    LogUtils.e(it.message)
                })
    }
}