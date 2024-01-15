package com.example.common_module.network.manager

import com.example.common_module.data.livedata.EventLiveData

class NetworkStateManager private constructor() {
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}