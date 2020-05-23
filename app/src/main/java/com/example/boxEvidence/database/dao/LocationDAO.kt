package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Location

@Dao
abstract class LocationDAO : GenericDAO<Location>() {

    @Query("SELECT * FROM locations")
    abstract fun getAll(): Array<Location>

    @Query("SELECT name FROM locations")
    abstract fun getAllNames(): Array<String>

    @Query("SELECT * FROM locations WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Location

    @Query("SELECT id FROM locations WHERE name = :name")
    abstract fun getIdByName(name: String) : Int
}