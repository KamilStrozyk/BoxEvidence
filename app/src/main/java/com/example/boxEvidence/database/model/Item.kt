package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "comment")
    val comment: String
)



