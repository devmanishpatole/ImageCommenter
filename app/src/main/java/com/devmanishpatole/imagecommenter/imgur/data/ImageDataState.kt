package com.devmanishpatole.imagecommenter.imgur.data

/**
 * Image loading states.
 */
sealed class ImageDataState {

    /**
     * Success state with list of images
     */
    class Success(val list: List<ImageData>) : ImageDataState()

    object NoInternet : ImageDataState()

    object ShowProgress : ImageDataState()

    object HideProgress : ImageDataState()

    object NoDataFound : ImageDataState()

    object UnknownError : ImageDataState()
}