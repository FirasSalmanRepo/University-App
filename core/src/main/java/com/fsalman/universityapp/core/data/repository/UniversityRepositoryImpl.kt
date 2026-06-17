package com.fsalman.universityapp.core.data.repository

import com.fsalman.universityapp.core.data.local.UniversityDao
import com.fsalman.universityapp.core.data.mapper.toDomain
import com.fsalman.universityapp.core.data.mapper.toEntity
import com.fsalman.universityapp.core.data.remote.UniversityApiService
import com.fsalman.universityapp.core.domain.model.UniversityResult
import com.fsalman.universityapp.core.domain.repository.UniversityRepository
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val apiService: UniversityApiService,
    private val dao: UniversityDao
) : UniversityRepository {

    override suspend fun getUniversities(): UniversityResult {
        return try {
            val dtos = apiService.getUniversities()
            val entities = dtos.map { it.toEntity() }
            dao.replaceAll(entities)
            UniversityResult(
                universities = entities.map { it.toDomain() },
                isFromCache = false
            )
        } catch (e: Exception) {
            val cached = dao.getAll()
            if (cached.isNotEmpty()) {
                UniversityResult(
                    universities = cached.map { it.toDomain() },
                    isFromCache = true
                )
            } else {
                throw e
            }
        }
    }
}
