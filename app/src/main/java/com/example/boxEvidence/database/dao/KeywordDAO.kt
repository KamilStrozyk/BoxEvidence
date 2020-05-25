package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Keyword

@Dao
abstract class KeywordDAO : GenericDAO<Keyword>() {

    @Query("SELECT * FROM keywords")
    abstract fun getAll(): Array<Keyword>

    @Query("SELECT * FROM keywords WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Keyword?

    @Query("SELECT * FROM keywords WHERE name = :name")
    abstract fun getByName(name: String) : Keyword?
}