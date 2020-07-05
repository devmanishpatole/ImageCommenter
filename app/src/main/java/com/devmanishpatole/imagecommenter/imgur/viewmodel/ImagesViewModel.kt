package com.devmanishpatole.imagecommenter.imgur.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmanishpatole.imagecommenter.base.BaseViewModel
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.data.ImageDataState
import com.devmanishpatole.imagecommenter.imgur.repository.ImagesRepository
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import com.devmanishpatole.imagecommenter.util.Result
import com.devmanishpatole.imagecommenter.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class ImagesViewModel @ViewModelInject constructor(
    networkHelper: NetworkHelper,
    private val imagesRepository: ImagesRepository
) : BaseViewModel(networkHelper) {

    private var imageData: List<ImageData>? = null
    private var query: String = ""

    private val _state = MutableLiveData<ImageDataState>()
    val state: LiveData<ImageDataState>
        get() = _state

    companion object {
        private const val DELAY = 250L
    }

    private fun fetchImages(search: String): Flow<Result<List<ImageData>>> {
        return flow {
            when {
                query == search -> {
                    emit(Result.success(imageData))
                }
                checkInternetConnection() -> {
                    query = search
                    emit(Result.loading())
                    val result = imagesRepository.fetchImageDetails(search)
                    if (result.status == Status.SUCCESS && null != result.data) {
                        imageData = result.data
                    }
                    emit(result)
                }
                else -> {
                    emit(Result.noInternetError())
                }
            }
        }
    }

    fun observeSearch(queryFlow: Flow<String>) {
        viewModelScope.launch {
            queryFlow
                .debounce(DELAY)
                .filter { query -> query.isNotEmpty() }
                .distinctUntilChanged()
                .flatMapLatest { query -> fetchImages(query) }
                .catch {
                    _state.value = ImageDataState.HideProgress
                    _state.value = ImageDataState.UnknownError
                }
                .collect { result ->
                    handleResult(result)
                }
        }
    }

    private fun handleResult(result: Result<List<ImageData>>) {
        when (result.status) {
            Status.SUCCESS -> {
                result.data?.let {
                    _state.value = ImageDataState.HideProgress
                    if (it.isEmpty()) {
                        _state.value = ImageDataState.NoDataFound
                    } else {
                        _state.value = ImageDataState.Success(it)
                    }
                } ?: run {
                    _state.value = ImageDataState.NoDataFound
                }
            }
            Status.LOADING -> _state.value = ImageDataState.ShowProgress
            Status.NO_INTERNET -> _state.value = ImageDataState.NoInternet
            else -> _state.value = ImageDataState.UnknownError
        }
    }

}