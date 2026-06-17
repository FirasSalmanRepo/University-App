package com.fsalman.universityapp.feature.listing.presentation

import com.fsalman.universityapp.core.domain.model.University

data class ListingState(
    val universities: List<University> = emptyList(),
    val isLoading: Boolean = false,
    val isFromCache: Boolean = false,
    val error: String? = null
)

sealed interface ListingIntent {
    data object LoadUniversities : ListingIntent
    data class SelectUniversity(val university: University) : ListingIntent
}

sealed interface ListingEffect {
    data class NavigateToDetails(val university: University) : ListingEffect
}
