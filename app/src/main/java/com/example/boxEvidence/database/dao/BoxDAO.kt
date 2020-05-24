package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Box

@Dao
abstract class BoxDAO : GenericDAO<Box>() {

    @Query("SELECT * FROM boxes")
    abstract fun getAll(): Array<Box>

    @Query("SELECT * FROM boxes WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Box

    @Query("SELECT * FROM boxes WHERE locationid = :entityId")
    abstract fun getByLocationId(entityId: Int) : Array<Box>

    @Query("SELECT * FROM boxes WHERE code = :code")
    abstract fun getByCode(code: String) : Array<Box>
}