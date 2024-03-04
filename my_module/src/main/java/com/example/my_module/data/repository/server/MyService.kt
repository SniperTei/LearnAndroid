package com.example.my_module.data.repository.server

import com.example.core_library.data.bean.AppResponse
import com.example.common_library.app.network.AppEnvironment
import com.example.core_library.network.AppNetworkAPI
import com.example.my_module.data.bean.MyInfoBean
import retrofit2.http.GET
import retrofit2.http.POST

val myService: MyService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    AppNetworkAPI.INSTANCE.getAPI(MyService::class.java, AppEnvironment.getBaseURL())
}
interface MyService {
    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getMyInfo(): AppResponse<MyInfoBean>

    /**
     * 登录
     */
    @POST("user/login")
    suspend fun login(): AppResponse<MyInfoBean>

    /**
     * 注册
     */
    @POST("user/register")
    suspend fun register(): AppResponse<MyInfoBean>

    /**
     * 退出
     */
    @GET("user/logout/json")
    suspend fun logout(): AppResponse<MyInfoBean>
}