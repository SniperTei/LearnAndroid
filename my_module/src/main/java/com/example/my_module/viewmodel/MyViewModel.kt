package com.example.my_module.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.common_library.app.base.viewmodel.BaseViewModel
import com.example.common_library.log.LogUtil
import com.example.my_module.data.repository.MyRepository
import kotlinx.coroutines.launch

class MyViewModel: BaseViewModel() {

    private val TAG = "MyViewModel"

    private val myRepository = MyRepository()
    fun getMyInfo() {
        viewModelScope.launch {
            runCatching {
                val resp = myRepository.getMyInfoFromServer()
                LogUtil.debugInfo(TAG, "resp : $resp")
            }.onSuccess {
                LogUtil.debugInfo(TAG, "my : $it")
            }.onFailure {
                LogUtil.debugInfo(TAG, "my error : $it")
            }

        }
    }
}