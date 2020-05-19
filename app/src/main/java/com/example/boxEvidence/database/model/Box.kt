package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "boxes")
data class Box(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val Id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "localizationid")
    val LocalizationId: Int,
    @ColumnInfo(name = "comment")
    val Comment: String,
    @ColumnInfo(name = "code")
    val Code: String,
    @ColumnInfo(name = "photoid")
    val PhotoId: Int
)
