package com.fsalman.universityapp.feature.listing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsalman.universityapp.core.domain.usecase.GetUniversitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    private val getUniversitiesUseCase: GetUniversitiesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListingState())
    val state: StateFlow<ListingState> = _state.asStateFlow()

    private val _effect = Channel<ListingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        handleIntent(ListingIntent.LoadUniversities)
    }

    fun handleIntent(intent: ListingIntent) {
        when (intent) {
            is ListingIntent.LoadUniversities -> loadUniversities()
            is ListingIntent.SelectUniversity -> {
                viewModelScope.launch {
                    _effect.send(ListingEffect.NavigateToDetails(intent.university))
                }
            }
        }
    }

    private fun loadUniversities() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val dataDeferred = async { getUniversitiesUseCase() }
                val timerDeferred = async { delay(MINIMUM_LOADING_MS) }
                val result = dataDeferred.await()
                timerDeferred.await()
                _state.update {
                    it.copy(
                        universities = result.universities,
                        isLoading = false,
                        isFromCache = result.isFromCache,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load universities"
                    )
                }
            }
        }
    }

    companion object {
        private const val MINIMUM_LOADING_MS = 4000L
    }
}
