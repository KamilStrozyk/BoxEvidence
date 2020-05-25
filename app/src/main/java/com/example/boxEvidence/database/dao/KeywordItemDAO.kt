package com.example.boxEvidence.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.boxEvidence.database.model.ItemKeywordCrossRef

@Dao
abstract  class KeywordItemDAO : GenericDAO<ItemKeywordCrossRef>() {

    @Query("SELECT * FROM itemkeyword")
    abstract fun getAll(): Array<ItemKeywordCrossRef>

    @Query("SELECT * FROM itemkeyword WHERE keywordId = :keywordid")
    abstract fun getByKeywordId(keywordid: Int) : Array<ItemKeywordCrossRef>

    @Query("SELECT * FROM itemkeyword WHERE itemId = :itemId")
    abstract fun getByItemId(itemId: Int) : Array<ItemKeywordCrossRef>
}