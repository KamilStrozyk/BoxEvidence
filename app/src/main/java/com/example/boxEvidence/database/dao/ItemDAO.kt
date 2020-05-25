package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.Item

@Dao
abstract class ItemDAO : GenericDAO<Item>() {

    @Query("SELECT * FROM items")
    abstract fun getAll(): Array<Item>

    @Query("SELECT * FROM items WHERE id = :entityId")
    abstract fun getById(entityId: Int): Item

    @Query("SELECT * FROM items WHERE boxid = :entityId")
    abstract fun getByBoxId(entityId: Int): Array<Item>

    @Query("SELECT * FROM items WHERE eancode = :code")
    abstract fun getByCode(code: String): Array<Item>

    @Query("SELECT * FROM items WHERE categoryid = :categoryId")
    abstract fun getByCategory(categoryId: Int): Array<Item>

    @Query("SELECT * FROM items WHERE name = :name AND boxid = :boxid AND comment = :comment")
    abstract fun getByValues(name: String, boxid: Int, comment: String): Item
}