package com.devmanishpatole.imagecommenter.imgur.service

import com.devmanishpatole.imagecommenter.imgur.data.ImageWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImgurService {

    @GET("3/gallery/search/1")
    suspend fun getImages(@Query("q") search: String): Response<ImageWrapper>
}