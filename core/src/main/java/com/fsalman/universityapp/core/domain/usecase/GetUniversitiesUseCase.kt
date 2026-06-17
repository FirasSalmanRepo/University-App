package com.fsalman.universityapp.core.domain.usecase

import com.fsalman.universityapp.core.domain.model.University
import com.fsalman.universityapp.core.domain.repository.UniversityRepository
import javax.inject.Inject

class GetUniversitiesUseCase @Inject constructor(
    private val repository: UniversityRepository
) {
    suspend operator fun invoke(): List<University> {
        return repository.getUniversities()
    }
}
