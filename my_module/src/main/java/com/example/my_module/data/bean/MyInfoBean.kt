package com.example.my_module.data.bean

import android.os.Parcelable

data class MyInfoBean(
    var name: String,
    var coinCount: Int,//当前积分
    var rank: Int,
    var userId: Int,
    var username: String
)
