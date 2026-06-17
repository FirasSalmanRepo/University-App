package com.fsalman.universityapp.core.domain.repository

import com.fsalman.universityapp.core.domain.model.University

interface UniversityRepository {
    suspend fun getUniversities(): List<University>
}
