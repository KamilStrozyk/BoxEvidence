package com.example.boxEvidence.database.model

import androidx.room.*


@Entity(
    tableName = "itemkeyword",
    primaryKeys = ["itemid", "keywordid"]
)
data class ItemKeywordCrossRef(
    @ColumnInfo(name = "itemid") val itemId: Int,
    @ColumnInfo(name = "keywordid") val keywordId: Int
)

data class ItemWithKeyword(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "itemid",
        entityColumn = "keywordid",
        associateBy = Junction(ItemKeywordCrossRef::class)
    )
    val keywords: List<Keyword>
)

data class KeywordWithItem(
    @Embedded val keyword: Keyword,
    @Relation(
        parentColumn = "keywordid",
        entityColumn = "itemid",
        associateBy = Junction(ItemKeywordCrossRef::class)
    )
    val items: List<Item>
)