package com.devmanishpatole.imagecommenter.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.devmanishpatole.imagecommenter.base.ViewModelProviderFactory
import com.devmanishpatole.imagecommenter.imgur.adapter.ImageListAdapter
import com.devmanishpatole.imagecommenter.imgur.repository.CommentRepository
import com.devmanishpatole.imagecommenter.imgur.repository.ImagesRepository
import com.devmanishpatole.imagecommenter.imgur.viewmodel.CommentViewModel
import com.devmanishpatole.imagecommenter.imgur.viewmodel.ImagesViewModel
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ExperimentalCoroutinesApi
    @FlowPreview
    @Provides
    fun provideImageDetailViewModel(
        networkHelper: NetworkHelper,
        imagesRepository: ImagesRepository,
        @ActivityContext context: Context
    ): ImagesViewModel = ViewModelProvider(
        context as AppCompatActivity, ViewModelProviderFactory(ImagesViewModel::class) {
            ImagesViewModel(
                networkHelper,
                imagesRepository
            )
        }).get(ImagesViewModel::class.java)

    @Provides
    fun provideCommentViewModel(
        networkHelper: NetworkHelper,
        commentRepository: CommentRepository,
        @ActivityContext context: Context
    ): CommentViewModel = ViewModelProvider(
        context as AppCompatActivity, ViewModelProviderFactory(CommentViewModel::class) {
            CommentViewModel(
                networkHelper,
                commentRepository
            )
        }).get(CommentViewModel::class.java)

    @Provides
    fun provideImageAdapter(@ActivityContext appContext: Context) =
        ImageListAdapter((appContext as AppCompatActivity).lifecycle, ArrayList())

}