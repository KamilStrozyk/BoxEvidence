package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "eancode")
    val eanCode: String?,
    @ColumnInfo(name = "categoryid")
    val categoryId: Int,
    @ColumnInfo(name = "boxid")
    val boxId: Int?,
    @ColumnInfo(name = "comment")
    val comment: String
)




