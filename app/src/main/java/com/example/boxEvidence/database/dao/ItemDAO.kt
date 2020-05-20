package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Item

@Dao
abstract class ItemDAO : GenericDAO<Item>() {

    @Query("SELECT * FROM items")
    abstract fun getAll(): Array<Item>

    @Query("SELECT * FROM items WHERE id = :entityId")
    abstract fun getById(entityId: Int) : Item

    @Query("SELECT * FROM items WHERE categoryid = :categoryId")
    abstract fun getByCategory(categoryId: Int) : Array<Item>
}