package com.devmanishpatole.imagecommenter.imgur.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.data.ImageDataState
import com.devmanishpatole.imagecommenter.imgur.repository.ImagesRepository
import com.devmanishpatole.imagecommenter.util.NetworkHelper
import com.devmanishpatole.imagecommenter.util.Result
import com.devmanishpatole.imagecommenter.util.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ImagesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var networkHelper: NetworkHelper

    lateinit var imagesRepository: ImagesRepository

    lateinit var viewModel: ImagesViewModel

    @Before
    fun setup() {
        networkHelper = mockk()
        imagesRepository = mockk()
        viewModel = ImagesViewModel(networkHelper, imagesRepository)
    }

    @Test
    fun fetchImages_whenNoInternet() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns false

        val dataFlow = invokeFetchImages("vanilla")

        dataFlow.collect { result ->
            assertNotNull(result)
            assertEquals(Status.NO_INTERNET, result.status)
        }
    }

    @Test
    fun fetchImages_whenDeviceRotate_shouldReturnStoredImages() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true

        val query = ImagesViewModel::class.java.getDeclaredField("query")
        query.isAccessible = true
        query.set(viewModel, "vanilla")

        val list = ImagesViewModel::class.java.getDeclaredField("imageData")
        list.isAccessible = true
        list.set(viewModel, getListOfImages())

        val dataFlow = invokeFetchImages("vanilla")

        dataFlow.collect { result ->
            assertNotNull(result)
            assertEquals(Status.SUCCESS, result.status)
            assertEquals(1, result.data?.size)
            assertEquals(10023, result.data?.get(0)?.datetime)
            assertEquals("testId", result.data?.get(0)?.id)
            assertEquals("testLink", result.data?.get(0)?.link)
            assertEquals("testTitle", result.data?.get(0)?.title)
        }
    }

    @Test
    fun fetchImages_whenResponseIsError_shouldReturnEmptyImages() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true
        coEvery { imagesRepository.fetchImageDetails(any()) } returns Result.error()

        val dataFlow = invokeFetchImages("vanilla")

        dataFlow.collect { result ->
            when (result.status) {
                Status.LOADING, Status.ERROR -> assertNull(result.data)
                Status.SUCCESS, Status.NO_INTERNET, Status.UNKNOWN -> fail()
            }
        }
    }

    @Test
    fun fetchImages_whenSuccess_shouldReturnImages() = runBlocking {
        every { networkHelper.isNetworkConnected() } returns true
        coEvery { imagesRepository.fetchImageDetails(any()) } returns Result.success(getListOfImages())

        val dataFlow = invokeFetchImages("vanilla")

        dataFlow.collect { result ->
            when (result.status) {
                Status.LOADING -> assertNull(result.data)
                Status.SUCCESS -> {
                    assertEquals(Status.SUCCESS, result.status)
                    assertEquals(1, result.data?.size)
                    assertEquals(10023, result.data?.get(0)?.datetime)
                    assertEquals("testId", result.data?.get(0)?.id)
                    assertEquals("testLink", result.data?.get(0)?.link)
                    assertEquals("testTitle", result.data?.get(0)?.title)
                }
                Status.NO_INTERNET, Status.UNKNOWN, Status.ERROR -> fail()
            }
        }
    }

    @Test
    fun handleResult_whenLoading_shouldUpdateLoading() {
        viewModel.state.observeForever {
            when (it) {
                is ImageDataState.ShowProgress -> return@observeForever
                is
                ImageDataState.Success,
                ImageDataState.HideProgress,
                ImageDataState.NoDataFound,
                ImageDataState.NoInternet,
                ImageDataState.UnknownError -> fail()
            }
        }

        invokeHandleResult(Result.loading())
    }

    @Test
    fun handleResult_whenNoInternet_shouldUpdateNoInternet() {
        viewModel.state.observeForever {
            when (it) {
                is ImageDataState.NoInternet -> return@observeForever
                is
                ImageDataState.Success,
                ImageDataState.HideProgress,
                ImageDataState.NoDataFound,
                ImageDataState.ShowProgress,
                ImageDataState.UnknownError -> fail()
            }
        }

        invokeHandleResult(Result.noInternetError())
    }

    @Test
    fun handleResult_whenError_shouldUpdateError() {
        viewModel.state.observeForever {
            when (it) {
                is ImageDataState.UnknownError -> return@observeForever
                is
                ImageDataState.Success,
                ImageDataState.HideProgress,
                ImageDataState.NoDataFound,
                ImageDataState.ShowProgress,
                ImageDataState.NoInternet -> fail()
            }
        }

        invokeHandleResult(Result.error())
    }

    @Test
    fun handleResult_whenSuccessWithEmptyData_shouldUpdateNoDataFound() {
        viewModel.state.observeForever {
            when (it) {
                is ImageDataState.HideProgress -> {
                    //Ignore
                }
                is ImageDataState.NoDataFound -> return@observeForever
                is
                ImageDataState.Success,
                ImageDataState.UnknownError,
                ImageDataState.ShowProgress,
                ImageDataState.NoInternet -> fail()
            }
        }

        invokeHandleResult(Result.success(listOf()))
    }

    @Test
    fun handleResult_whenSuccessWithData_shouldUpdateData() {
        viewModel.state.observeForever {
            when (it) {
                is ImageDataState.HideProgress -> {
                    //Ignore
                }
                is ImageDataState.Success -> {
                    assertEquals(1, it.list.size)
                    assertEquals(10023, it.list[0].datetime)
                    assertEquals("testId", it.list[0].id)
                    assertEquals("testLink", it.list[0].link)
                    assertEquals("testTitle", it.list[0].title)
                }
                is
                ImageDataState.NoDataFound,
                ImageDataState.UnknownError,
                ImageDataState.ShowProgress,
                ImageDataState.NoInternet -> fail()
            }
        }

        invokeHandleResult(Result.success(getListOfImages()))
    }


    private fun invokeFetchImages(search: String): Flow<Result<List<ImageData>>> {
        val methodFetchData =
            ImagesViewModel::class.java.getDeclaredMethod("fetchImages", String::class.java)
        methodFetchData.isAccessible = true
        return methodFetchData.invoke(viewModel, search) as Flow<Result<List<ImageData>>>
    }

    private fun invokeHandleResult(result: Result<List<ImageData>>) {
        val methodHandleResult =
            ImagesViewModel::class.java.getDeclaredMethod("handleResult", Result::class.java)
        methodHandleResult.isAccessible = true
        methodHandleResult.invoke(viewModel, result)
    }

    private fun getListOfImages() = listOf(
        ImageData(10023, "testId", "testLink", "testTitle")
    )

}