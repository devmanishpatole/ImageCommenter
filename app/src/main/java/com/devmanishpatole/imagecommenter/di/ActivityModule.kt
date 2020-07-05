package com.devmanishpatole.imagecommenter.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.devmanishpatole.imagecommenter.imgur.adapter.ImageListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideImageAdapter(@ActivityContext appContext: Context) =
        ImageListAdapter((appContext as AppCompatActivity).lifecycle, ArrayList())

}