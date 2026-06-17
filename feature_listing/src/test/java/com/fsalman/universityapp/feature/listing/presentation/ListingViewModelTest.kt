package com.fsalman.universityapp.feature.listing.presentation

import com.fsalman.universityapp.core.domain.model.University
import com.fsalman.universityapp.core.domain.model.UniversityResult
import com.fsalman.universityapp.core.domain.usecase.GetUniversitiesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getUniversitiesUseCase: GetUniversitiesUseCase

    private val sampleUniversities = listOf(
        University(
            name = "University A",
            country = "United Arab Emirates",
            stateProvince = null,
            alphaTwoCode = "AE",
            webPages = listOf("http://a.ac.ae"),
            domains = listOf("a.ac.ae")
        ),
        University(
            name = "University B",
            country = "United Arab Emirates",
            stateProvince = "Dubai",
            alphaTwoCode = "AE",
            webPages = listOf("http://b.ac.ae"),
            domains = listOf("b.ac.ae")
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getUniversitiesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state loads universities successfully`() = runTest(testDispatcher) {
        coEvery { getUniversitiesUseCase() } returns UniversityResult(sampleUniversities, false)

        val viewModel = ListingViewModel(getUniversitiesUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(sampleUniversities, state.universities)
        assertFalse(state.isLoading)
        assertFalse(state.isFromCache)
        assertNull(state.error)
    }

    @Test
    fun `initial state shows error on failure`() = runTest(testDispatcher) {
        coEvery { getUniversitiesUseCase() } throws RuntimeException("Network error")

        val viewModel = ListingViewModel(getUniversitiesUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.universities.isEmpty())
        assertFalse(state.isLoading)
        assertEquals("Network error", state.error)
    }

    @Test
    fun `LoadUniversities intent refreshes data`() = runTest(testDispatcher) {
        coEvery { getUniversitiesUseCase() } returns UniversityResult(sampleUniversities, false)

        val viewModel = ListingViewModel(getUniversitiesUseCase)
        advanceUntilIdle()

        coEvery { getUniversitiesUseCase() } returns UniversityResult(sampleUniversities.take(1), false)
        viewModel.handleIntent(ListingIntent.LoadUniversities)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(1, state.universities.size)
    }

    @Test
    fun `cached data sets isFromCache flag`() = runTest(testDispatcher) {
        coEvery { getUniversitiesUseCase() } returns UniversityResult(sampleUniversities, true)

        val viewModel = ListingViewModel(getUniversitiesUseCase)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.isFromCache)
        assertEquals(sampleUniversities, state.universities)
    }

    @Test
    fun `SelectUniversity intent emits navigation effect`() = runTest(testDispatcher) {
        coEvery { getUniversitiesUseCase() } returns UniversityResult(sampleUniversities, false)

        val viewModel = ListingViewModel(getUniversitiesUseCase)
        advanceUntilIdle()

        val effects = mutableListOf<ListingEffect>()
        val job = kotlinx.coroutines.launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.handleIntent(ListingIntent.SelectUniversity(sampleUniversities[0]))
        advanceUntilIdle()

        assertTrue(effects.isNotEmpty())
        assertTrue(effects[0] is ListingEffect.NavigateToDetails)
        assertEquals(
            sampleUniversities[0],
            (effects[0] as ListingEffect.NavigateToDetails).university
        )

        job.cancel()
    }
}
