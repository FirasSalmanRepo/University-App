package com.fsalman.universityapp.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import com.fsalman.universityapp.core.domain.model.University
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val sampleUniversity = University(
        name = "Test University",
        country = "United Arab Emirates",
        stateProvince = null,
        alphaTwoCode = "AE",
        webPages = listOf("http://test.ac.ae"),
        domains = listOf("test.ac.ae")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state contains university from SavedStateHandle`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(
            mapOf(DetailsViewModel.EXTRA_UNIVERSITY to sampleUniversity)
        )

        val viewModel = DetailsViewModel(savedStateHandle)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertNotNull(state.university)
        assertEquals(sampleUniversity.name, state.university?.name)
    }

    @Test
    fun `Refresh intent emits NavigateBackWithRefresh effect`() = runTest(testDispatcher) {
        val savedStateHandle = SavedStateHandle(
            mapOf(DetailsViewModel.EXTRA_UNIVERSITY to sampleUniversity)
        )
        val viewModel = DetailsViewModel(savedStateHandle)
        advanceUntilIdle()

        val effects = mutableListOf<DetailsEffect>()
        val job = kotlinx.coroutines.launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.handleIntent(DetailsIntent.Refresh)
        advanceUntilIdle()

        assertTrue(effects.isNotEmpty())
        assertTrue(effects[0] is DetailsEffect.NavigateBackWithRefresh)

        job.cancel()
    }
}
