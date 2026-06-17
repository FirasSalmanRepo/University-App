package com.fsalman.universityapp.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApiService {

    @GET("search")
    suspend fun getUniversities(
        @Query("country") country: String = "United Arab Emirates"
    ): List<UniversityDto>
}
