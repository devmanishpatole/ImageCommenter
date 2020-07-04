package com.devmanishpatole.imagecommenter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CommentDao {
    @Insert(onConflict = REPLACE)
    suspend fun save(comment: Comment)

    @Query("SELECT * FROM comment WHERE id = :id")
    suspend fun getComment(id: String): Comment
}