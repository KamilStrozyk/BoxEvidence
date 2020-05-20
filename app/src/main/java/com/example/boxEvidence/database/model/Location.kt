package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "locations")
data class Location  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String)

data class LocationWithBoxes(
    @Embedded val location: Location,
    @Relation(
        parentColumn = "id",
        entityColumn = "locationid"
    )
    val boxes: List<Box>
)
