package com.example.my_module.data.repository.server

import com.example.common_library.app.base.bean.AppResponse
import com.example.common_library.app.network.AppEnvironment
import com.example.common_library.app.network.AppNetworkAPI
import com.example.my_module.data.bean.MyInfoBean
import retrofit2.http.GET

val myService: MyService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    AppNetworkAPI.INSTANCE.getAPI(MyService::class.java, AppEnvironment.getBaseURL())
}
interface MyService {
    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getMyInfo(): AppResponse<MyInfoBean>
}