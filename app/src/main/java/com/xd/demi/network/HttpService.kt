package com.xd.demi.network

import com.xd.demi.bean.Data
import com.xd.demi.bean.Event
import com.xd.demi.bean.HttpResp
import com.xd.demi.bean.User
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * 服务接口，服务地址默认的前缀是base_api，如果不是以base_api开头的url需要指定@Scope
 * Created by tong on 2017/12/21.
 */
interface HttpService {

    @GET("lg/todo/list/{type}/json")
    fun getToadListByType(@Path("type") type: Int): Flowable<HttpResp<Data>>


    @POST("user/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<HttpResp<User>>

    @POST("lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun update(@Path("id") id: Long, @Field("title") title: String?, @Field("content") content: String?, @Field("status") status: Int, @Field("type") type: Int, @Field("date") date: String?): Flowable<HttpResp<Event>>

}