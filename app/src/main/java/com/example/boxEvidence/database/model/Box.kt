package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "boxes")
data class Box(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "locationid")
    val locationId: Int,
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "photoid")
    val photoId: Int?
)

data class BoxWithItems(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "id",
        entityColumn = "boxid"
    )
    val items: List<Item>
)

