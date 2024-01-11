package com.example.learnandroid.home.model.bean

data class HomeDataBean<T>(
    var curPage: Int,
    var datas: T,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)
