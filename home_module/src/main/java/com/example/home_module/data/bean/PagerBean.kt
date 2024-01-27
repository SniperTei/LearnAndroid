package com.example.home_module.data.bean

data class PagerBean<T>(
    var curPage: Int,
    var datas: T,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)
