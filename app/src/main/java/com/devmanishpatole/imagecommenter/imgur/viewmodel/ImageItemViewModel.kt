package com.devmanishpatole.imagecommenter.imgur.viewmodel

import com.devmanishpatole.imagecommenter.base.BaseItemViewModel
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import javax.inject.Inject

/**
 * ViewModel to handle operation specific to element in list.
 */
class ImageItemViewModel @Inject constructor(networkHelper: NetworkHelper) :
    BaseItemViewModel<ImageData>(networkHelper)