package com.xd.demi.network

import com.blankj.utilcode.util.LogUtils
import com.orhanobut.hawk.Hawk
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by tong on 2017/12/19.
 */
object RetrofitManager {
    val baseUrl: String = "http://wanandroid.com/"
    val client: OkHttpClient by lazy { createOkHttpClient() }

    val model: HttpService by lazy { getRetrofit(baseUrl).create(HttpService::class.java) }

    private fun createOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .writeTimeout(30L, TimeUnit.SECONDS)
                .cookieJar(object : CookieJar {
                    override fun saveFromResponse(url: HttpUrl?, cookies: MutableList<Cookie>?) {
                        if (url?.toString()?.contains("user/login") == true) {
                            Hawk.put("cookies", cookies)
                        }
                        cookies?.forEach {
                            LogUtils.d("cookie:", "expire" + it.expiresAt() + "cookie name:" + it.name() + "cookie value" + it.value())
                        }
                    }

                    override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
                        return Hawk.get<MutableList<Cookie>>("cookies") ?: ArrayList()
                    }
                })
                .build()
    }

    private fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

}

