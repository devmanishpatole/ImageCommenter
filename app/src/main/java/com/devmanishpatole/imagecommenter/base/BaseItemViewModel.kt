package com.devmanishpatole.imagecommenter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmanishpatole.imagecommenter.util.NetworkHelper

abstract class BaseItemViewModel<T>(networkHelper: NetworkHelper) : BaseViewModel(networkHelper) {

    private val _data = MutableLiveData<T>()
    val data: LiveData<T>
        get() = _data


    open fun updateData(data: T) {
        _data.postValue(data)
    }

    fun onManualClear() = onCleared()
}