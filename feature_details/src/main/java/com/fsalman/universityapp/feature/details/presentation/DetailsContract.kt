package com.fsalman.universityapp.feature.details.presentation

import com.fsalman.universityapp.core.domain.model.University

data class DetailsState(
    val university: University? = null
)

sealed interface DetailsIntent {
    data object Refresh : DetailsIntent
}

sealed interface DetailsEffect {
    data object NavigateBackWithRefresh : DetailsEffect
}
