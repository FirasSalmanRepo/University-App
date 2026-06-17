package com.fsalman.universityapp.core.domain.repository

import com.fsalman.universityapp.core.domain.model.UniversityResult

interface UniversityRepository {
    suspend fun getUniversities(): UniversityResult
}
