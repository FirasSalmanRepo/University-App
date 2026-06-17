package com.fsalman.universityapp.feature.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fsalman.universityapp.core.domain.model.University
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state: StateFlow<DetailsState> = _state.asStateFlow()

    private val _effect = Channel<DetailsEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        val university = savedStateHandle.get<University>(EXTRA_UNIVERSITY)
        _state.update { it.copy(university = university) }
    }

    fun handleIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.Refresh -> {
                viewModelScope.launch {
                    _effect.send(DetailsEffect.NavigateBackWithRefresh)
                }
            }
        }
    }

    companion object {
        const val EXTRA_UNIVERSITY = "extra_university"
    }
}
