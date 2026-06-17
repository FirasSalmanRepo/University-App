package com.fsalman.universityapp.core.domain.model

data class UniversityResult(
    val universities: List<University>,
    val isFromCache: Boolean
)
