package com.fsalman.universityapp.core.data.repository

import com.fsalman.universityapp.core.data.local.UniversityDao
import com.fsalman.universityapp.core.data.mapper.toDomain
import com.fsalman.universityapp.core.data.mapper.toEntity
import com.fsalman.universityapp.core.data.remote.UniversityApiService
import com.fsalman.universityapp.core.domain.model.University
import com.fsalman.universityapp.core.domain.repository.UniversityRepository
import javax.inject.Inject

class UniversityRepositoryImpl @Inject constructor(
    private val apiService: UniversityApiService,
    private val dao: UniversityDao
) : UniversityRepository {

    override suspend fun getUniversities(): List<University> {
        return try {
            val dtos = apiService.getUniversities()
            val entities = dtos.map { it.toEntity() }
            dao.replaceAll(entities)
            entities.map { it.toDomain() }
        } catch (e: Exception) {
            val cached = dao.getAll()
            if (cached.isNotEmpty()) {
                cached.map { it.toDomain() }
            } else {
                throw e
            }
        }
    }
}
