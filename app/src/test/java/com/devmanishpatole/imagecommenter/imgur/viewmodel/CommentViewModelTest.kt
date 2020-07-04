package com.devmanishpatole.imagecommenter.imgur.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmanishpatole.imagecommenter.CoroutinesTestRule
import com.devmanishpatole.imagecommenter.database.Comment
import com.devmanishpatole.imagecommenter.imgur.repository.CommentRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CommentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    lateinit var repository: CommentRepository

    lateinit var viewModel: CommentViewModel

    @Before
    fun setup() {
        repository = mockk()
        viewModel = CommentViewModel(mockk(), repository)
    }

    @Test
    fun testSaveComment() = coroutinesTestRule.testDispatcher.runBlockingTest {
        coEvery { repository.saveComment(any(), any()) } just Runs
        viewModel.saveComment("id", "text")

        coVerify { repository.saveComment(any(), any()) }
    }

    @Test
    fun testGetComment() = coroutinesTestRule.testDispatcher.runBlockingTest {
        val comment = Comment("id", "text")
        coEvery { repository.getComment(any()) } returns comment

        viewModel.commentData.observeForever {
            assertNotNull(it)
            assertEquals("id", it.id)
            assertEquals("text", it.comment)
        }

        viewModel.getComment("id")
        coVerify { repository.getComment(any()) }
    }
}