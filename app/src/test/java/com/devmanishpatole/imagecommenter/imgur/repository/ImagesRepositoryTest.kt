package com.devmanishpatole.imagecommenter.imgur.repository

import com.devmanishpatole.imagecommenter.imgur.data.ImageData
import com.devmanishpatole.imagecommenter.imgur.data.ImageWrapper
import com.devmanishpatole.imagecommenter.imgur.service.ImgurService
import com.devmanishpatole.imagecommenter.util.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ImagesRepositoryTest {

    lateinit var imgurService: ImgurService

    lateinit var repo: ImagesRepository

    @Before
    fun setup() {
        imgurService = mockk()
        repo = ImagesRepository(imgurService)
    }

    @Test
    fun testFetchImageDetails_withServiceFailure() = runBlocking {
        val response: Response<ImageWrapper> = mockk()

        every { response.body() } returns mockk()
        every { response.isSuccessful } returns false
        coEvery { imgurService.getImages(any()) } returns response

        val result = repo.fetchImageDetails("")

        assertEquals(Status.ERROR, result.status)
        assertNull(result.data)
    }

    @Test
    fun testFetchImageDetails_withServiceSuccessButDataFailure() = runBlocking {
        val response: Response<ImageWrapper> = mockk()

        val wrapper = ImageWrapper(getListOfImages(), false, 400)

        every { response.body() } returns wrapper
        every { response.isSuccessful } returns true
        coEvery { imgurService.getImages(any()) } returns response

        val result = repo.fetchImageDetails("")

        assertEquals(Status.ERROR, result.status)
        assertNull(result.data)
    }

    @Test
    fun testFetchImageDetails_withSuccess() = runBlocking {
        val response: Response<ImageWrapper> = mockk()

        val wrapper = ImageWrapper(getListOfImages(), true, 400)

        every { response.body() } returns wrapper
        every { response.isSuccessful } returns true
        coEvery { imgurService.getImages(any()) } returns response

        val result = repo.fetchImageDetails("")

        assertEquals(Status.SUCCESS, result.status)
        assertNotNull(result.data)
        val data = result.data
        assertEquals(1, data?.size)
        assertEquals(10023, data?.get(0)?.datetime)
        assertEquals("testId", data?.get(0)?.id)
        assertEquals("testLink", data?.get(0)?.link)
        assertEquals("testTitle", data?.get(0)?.title)
    }


    private fun getListOfImages() = listOf(
        ImageData(10023, "testId", "testLink", "testTitle")
    )

}