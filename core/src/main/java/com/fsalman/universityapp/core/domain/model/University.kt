package com.fsalman.universityapp.core.domain.model

import java.io.Serializable

data class University(
    val name: String,
    val country: String,
    val stateProvince: String?,
    val alphaTwoCode: String,
    val webPages: List<String>,
    val domains: List<String>
) : Serializable
