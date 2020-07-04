package com.devmanishpatole.imagecommenter.imgur.repository

import com.devmanishpatole.imagecommenter.database.Comment
import com.devmanishpatole.imagecommenter.database.CommentDao
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CommentRepositoryTest {

    lateinit var commentDao: CommentDao

    lateinit var repo: CommentRepository

    @Before
    fun setup() {
        commentDao = mockk()
        repo = CommentRepository(commentDao)
    }

    @Test
    fun testSaveComment() = runBlocking {
        coEvery { commentDao.save(any()) } just Runs
        repo.saveComment("id", "text")
        coVerify { commentDao.save(any()) }
        confirmVerified(commentDao)
    }

    @Test
    fun testGetComment() = runBlocking {
        val comment = Comment("id", "text")
        coEvery { commentDao.getComment(any()) } returns comment
        val returnComment = repo.getComment("id")

        assertNotNull(returnComment)
        assertEquals("id", returnComment.id)
        assertEquals("text", returnComment.comment)
        coVerify { commentDao.getComment("id") }
        confirmVerified(commentDao)
    }
}