package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "eancode")
    var eanCode: String?,
    @ColumnInfo(name = "categoryid")
    val categoryId: Int,
    @ColumnInfo(name = "boxid")
    var boxId: Int?,
    @ColumnInfo(name = "comment")
    var comment: String
)




