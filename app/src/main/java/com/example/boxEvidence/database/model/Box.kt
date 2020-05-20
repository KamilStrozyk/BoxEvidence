package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "boxes")
data class Box(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "localizationid")
    val localizationId: Int,
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "photoid")
    val photoId: Int?
)
