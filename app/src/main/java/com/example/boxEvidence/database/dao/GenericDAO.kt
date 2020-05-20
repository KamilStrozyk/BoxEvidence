package com.example.boxEvidence.database.dao

import androidx.room.*

interface GenericDAO<T> {
    val tableName : String

    @Insert()
    fun insert(vararg obj: T)

    @Insert()
    fun insert(vararg obj: List<T>)

    @Update()
    fun update(vararg obj: T)

    @Update()
    fun update(vararg obj: List<T>)

    @Delete()
    fun delete(vararg obj: T)

    @Delete()
    fun delete(vararg obj: List<T>)
}

