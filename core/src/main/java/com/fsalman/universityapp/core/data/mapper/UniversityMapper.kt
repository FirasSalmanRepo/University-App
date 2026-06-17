package com.fsalman.universityapp.core.data.mapper

import com.fsalman.universityapp.core.data.local.UniversityEntity
import com.fsalman.universityapp.core.data.remote.UniversityDto
import com.fsalman.universityapp.core.domain.model.University

fun UniversityDto.toEntity(): UniversityEntity {
    return UniversityEntity(
        name = name,
        country = country,
        stateProvince = stateProvince,
        alphaTwoCode = alphaTwoCode,
        webPages = webPages,
        domains = domains
    )
}

fun UniversityEntity.toDomain(): University {
    return University(
        name = name,
        country = country,
        stateProvince = stateProvince,
        alphaTwoCode = alphaTwoCode,
        webPages = webPages,
        domains = domains
    )
}
