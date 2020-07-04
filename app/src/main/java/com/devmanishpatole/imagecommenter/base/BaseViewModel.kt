package com.devmanishpatole.imagecommenter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import com.devmanishpatole.imagecommenter.util.Result


abstract class BaseViewModel(
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _messageStringId: MutableLiveData<Result<Int>> = MutableLiveData()
    val messageStringId: LiveData<Result<Int>>
        get() = _messageStringId

    private val _messageString: MutableLiveData<Result<String>> = MutableLiveData()
    val messageString: LiveData<Result<String>>
        get() = _messageString

    protected fun checkInternetConnection() = networkHelper.isNetworkConnected()

}