package com.fsalman.universityapp.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class UniversityDao {

    @Query("SELECT * FROM universities ORDER BY name ASC")
    abstract suspend fun getAll(): List<UniversityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(universities: List<UniversityEntity>)

    @Query("DELETE FROM universities")
    abstract suspend fun clearAll()

    @Transaction
    open suspend fun replaceAll(universities: List<UniversityEntity>) {
        clearAll()
        insertAll(universities)
    }
}
