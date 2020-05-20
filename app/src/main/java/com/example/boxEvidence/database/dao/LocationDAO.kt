package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Location

@Dao
abstract class LocationDAO : GenericDAO<Location>{

    @Query("SELECT * FROM locations")
    abstract fun getAll(): Array<Location>

    @Query("SELECT * FROM locations WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Location
}