package com.example.common_library.network.manager

import com.example.common_library.data.livedata.EventLiveData

class NetworkStateManager private constructor() {
    val mNetworkStateCallback = EventLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}