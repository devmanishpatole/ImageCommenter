package com.devmanishpatole.imagecommenter.imgur.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devmanishpatole.imagecommenter.base.BaseViewModel
import com.devmanishpatole.imagecommenter.database.Comment
import com.devmanishpatole.imagecommenter.imgur.repository.CommentRepository
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import kotlinx.coroutines.launch

class CommentViewModel @ViewModelInject constructor(
    networkHelper: NetworkHelper,
    private val repository: CommentRepository
) :
    BaseViewModel(networkHelper) {

    private val _commentData = MutableLiveData<Comment>()
    val commentData: LiveData<Comment>
        get() = _commentData


    fun saveComment(id: String, text: String) {
        viewModelScope.launch {
            repository.saveComment(id, text)
        }
    }

    fun getComment(id: String) {
        viewModelScope.launch {
            _commentData.value = repository.getComment(id)
        }
    }

}