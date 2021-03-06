package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Photo

@Dao
abstract class PhotoDAO : GenericDAO<Photo>() {

    @Query("SELECT * FROM photos")
    abstract fun getAll(): Array<Photo>

    @Query("SELECT id FROM locations")
    abstract fun getAllIds(): Array<Int>

    @Query("SELECT * FROM photos WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Photo

    @Query("SELECT * FROM photos WHERE itemid = :entityId")
    abstract fun getByItemId(entityId: Int) : List<Photo>
}