package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val Id: Int,
    @ColumnInfo(name = "name")
    val Name: String,
    @ColumnInfo(name = "eancode")
    val EANCode: String,
    @ColumnInfo(name = "categoryid")
    val CategoryId: Int,
    @ColumnInfo(name = "comment")
    val Comment: String
)

