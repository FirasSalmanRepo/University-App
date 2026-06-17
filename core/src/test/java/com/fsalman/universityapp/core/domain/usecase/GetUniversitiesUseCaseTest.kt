package com.fsalman.universityapp.core.domain.usecase

import com.fsalman.universityapp.core.domain.model.University
import com.fsalman.universityapp.core.domain.repository.UniversityRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUniversitiesUseCaseTest {

    private lateinit var repository: UniversityRepository
    private lateinit var useCase: GetUniversitiesUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetUniversitiesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() = runTest {
        val universities = listOf(
            University(
                name = "Test University",
                country = "United Arab Emirates",
                stateProvince = null,
                alphaTwoCode = "AE",
                webPages = listOf("http://test.ac.ae"),
                domains = listOf("test.ac.ae")
            )
        )
        coEvery { repository.getUniversities() } returns universities

        val result = useCase()

        assertEquals(universities, result)
        coVerify(exactly = 1) { repository.getUniversities() }
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        coEvery { repository.getUniversities() } throws RuntimeException("Error")

        val exception = try {
            useCase()
            null
        } catch (e: Exception) {
            e
        }

        assertEquals("Error", exception?.message)
    }
}
