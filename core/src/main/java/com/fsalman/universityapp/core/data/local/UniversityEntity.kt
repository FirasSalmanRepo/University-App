package com.fsalman.universityapp.core.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "universities")
data class UniversityEntity(
    @PrimaryKey
    val name: String,
    val country: String,
    @ColumnInfo(name = "state_province")
    val stateProvince: String?,
    @ColumnInfo(name = "alpha_two_code")
    val alphaTwoCode: String,
    @ColumnInfo(name = "web_pages")
    val webPages: List<String>,
    val domains: List<String>
)
