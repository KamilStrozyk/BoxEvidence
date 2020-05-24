package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "boxes")
data class Box(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "locationid")
    var locationId: Int,
    @ColumnInfo(name = "comment")
    var comment: String,
    @ColumnInfo(name = "code")
    var code: String,
    @ColumnInfo(name = "photoid")
    var photoId: Int?
)

data class BoxWithItems(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "id",
        entityColumn = "boxid"
    )
    val items: List<Item>
)

