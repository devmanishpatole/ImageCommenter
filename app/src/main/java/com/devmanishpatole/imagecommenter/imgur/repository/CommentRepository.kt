package com.devmanishpatole.imagecommenter.imgur.repository

import com.devmanishpatole.imagecommenter.database.Comment
import com.devmanishpatole.imagecommenter.database.CommentDao
import javax.inject.Inject

class CommentRepository @Inject constructor(private val commentDao: CommentDao) {

    suspend fun saveComment(id: String, text: String) = commentDao.save(Comment(id, text))

    suspend fun getComment(id: String) = commentDao.getComment(id)
}