package com.devmanishpatole.imagecommenter.imgur.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    val datetime: Int,
    val id: String,
    val link: String,
    val title: String
) : Parcelable {
}