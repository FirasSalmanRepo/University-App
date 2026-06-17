package com.fsalman.universityapp.core.data.repository

import com.fsalman.universityapp.core.data.local.UniversityDao
import com.fsalman.universityapp.core.data.local.UniversityEntity
import com.fsalman.universityapp.core.data.remote.UniversityApiService
import com.fsalman.universityapp.core.data.remote.UniversityDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UniversityRepositoryImplTest {

    private lateinit var apiService: UniversityApiService
    private lateinit var dao: UniversityDao
    private lateinit var repository: UniversityRepositoryImpl

    @Before
    fun setup() {
        apiService = mockk()
        dao = mockk(relaxed = true)
        repository = UniversityRepositoryImpl(apiService, dao)
    }

    @Test
    fun `getUniversities returns API data and caches it on success`() = runTest {
        val dtos = listOf(
            UniversityDto(
                name = "Test University",
                country = "United Arab Emirates",
                stateProvince = null,
                alphaTwoCode = "AE",
                webPages = listOf("http://test.ac.ae"),
                domains = listOf("test.ac.ae")
            )
        )
        coEvery { apiService.getUniversities(any()) } returns dtos

        val result = repository.getUniversities()

        assertEquals(1, result.size)
        assertEquals("Test University", result[0].name)
        coVerify { dao.replaceAll(any()) }
    }

    @Test
    fun `getUniversities returns cached data on API failure`() = runTest {
        val cachedEntities = listOf(
            UniversityEntity(
                name = "Cached University",
                country = "United Arab Emirates",
                stateProvince = null,
                alphaTwoCode = "AE",
                webPages = listOf("http://cached.ac.ae"),
                domains = listOf("cached.ac.ae")
            )
        )
        coEvery { apiService.getUniversities(any()) } throws IOException("Network error")
        coEvery { dao.getAll() } returns cachedEntities

        val result = repository.getUniversities()

        assertEquals(1, result.size)
        assertEquals("Cached University", result[0].name)
    }

    @Test
    fun `getUniversities throws when API fails and cache is empty`() = runTest {
        coEvery { apiService.getUniversities(any()) } throws IOException("Network error")
        coEvery { dao.getAll() } returns emptyList()

        val exception = try {
            repository.getUniversities()
            null
        } catch (e: Exception) {
            e
        }

        assertTrue(exception is IOException)
    }
}
