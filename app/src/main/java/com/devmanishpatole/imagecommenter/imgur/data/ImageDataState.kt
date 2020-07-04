package com.devmanishpatole.imagecommenter.imgur.data

sealed class ImageDataState {

    class Success(val list: List<ImageData>) : ImageDataState()

    object NoInternet : ImageDataState()

    object ShowProgress : ImageDataState()

    object HideProgress : ImageDataState()

    object NoDataFound : ImageDataState()

    object UnknownError : ImageDataState()
}