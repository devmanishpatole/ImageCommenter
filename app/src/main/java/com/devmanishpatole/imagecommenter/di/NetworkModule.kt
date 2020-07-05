package com.devmanishpatole.imagecommenter.di

import android.content.Context
import com.devmanishpatole.imagecommenter.BuildConfig
import com.devmanishpatole.imagecommenter.imgur.service.ImgurService
import com.devmanishpatole.imagecommenter.network.Networking
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    private const val AUTHORIZATION = "Authorization"
    private const val AUTHORIZATION_VALUE = "Client-ID 137cda6b5008a7c"

    @Provides
    @Singleton
    fun provideAuthenticatorInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder()
                .header(
                    AUTHORIZATION,
                    AUTHORIZATION_VALUE
                )
                .build()
            return chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideNetwork(
        @ApplicationContext appContext: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): Retrofit =
        Networking.create(
            BuildConfig.BASE_URL,
            appContext.cacheDir,
            10 * 1024 * 1024, // 10MB
            loggingInterceptor,
            interceptor
        )

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext appContext: Context) = NetworkHelper(appContext)

    @Provides
    @Singleton
    fun provideImgurService(retrofit: Retrofit): ImgurService =
        retrofit.create(ImgurService::class.java)

}