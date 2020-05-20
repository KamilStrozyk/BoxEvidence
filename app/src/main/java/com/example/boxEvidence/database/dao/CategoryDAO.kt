package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Category

@Dao
abstract class CategoryDAO : GenericDAO<Category>{

    @Query("SELECT * FROM categories")
    abstract fun getAll(): Array<Category>

    @Query("SELECT * FROM categories WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Category
}