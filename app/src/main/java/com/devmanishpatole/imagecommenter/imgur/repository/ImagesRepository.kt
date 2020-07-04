package com.devmanishpatole.imagecommenter.imgur.repository

import com.devmanishpatole.imagecommenter.imgur.service.ImgurService
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.util.Result
import javax.inject.Inject

class ImagesRepository @Inject constructor(private val imgurService: ImgurService) {

    suspend fun fetchImageDetails(search: String): Result<List<ImageData>> {
        val response = imgurService.getImages(search)
        val imageDetail = response.body()

        return if (response.isSuccessful && imageDetail?.success == true) {
            Result.success(imageDetail.data)
        } else {
            Result.error()
        }
    }
}