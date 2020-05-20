package com.example.boxEvidence.database.dao

import androidx.room.*


@Dao
abstract class GenericDAO<T> {

    @Insert()
    abstract fun add(obj: T)

    @Insert()
    abstract fun add(obj: List<T>)

    @Update()
    abstract fun updateElem(obj: T)

    @Update()
    abstract fun updateElem(obj: List<T>)

    @Delete()
    abstract fun remove(obj: T)

    @Delete()
    abstract fun remove(obj: List<T>)
}

