package com.devmanishpatole.imagecommenter.di

import android.content.Context
import androidx.room.Room
import com.devmanishpatole.imagecommenter.database.CommentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object ApplicationModule {

    private const val DATABASE_NAME = "Commenter-Database"

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CommentDatabase::class.java, DATABASE_NAME
    ).build()

    @Provides
    fun provideCommentDao(db: CommentDatabase) = db.commentDao()
}