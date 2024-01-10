package com.example.learnandroid.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    private val titleName = MutableLiveData<String>()

    fun changeTitleName(name: String) {
        titleName.value = name
    }

    fun getTitleName(): LiveData<String> {
        return titleName
    }
}