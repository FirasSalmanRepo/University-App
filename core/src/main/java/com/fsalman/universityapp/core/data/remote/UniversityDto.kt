package com.fsalman.universityapp.core.data.remote

import com.google.gson.annotations.SerializedName

data class UniversityDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("state-province")
    val stateProvince: String?,
    @SerializedName("alpha_two_code")
    val alphaTwoCode: String,
    @SerializedName("web_pages")
    val webPages: List<String>,
    @SerializedName("domains")
    val domains: List<String>
)
