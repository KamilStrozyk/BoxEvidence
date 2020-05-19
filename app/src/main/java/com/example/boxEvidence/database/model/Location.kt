package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "locations")
data class Location  (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val Id: Int,
    @ColumnInfo(name = "name")
    val Name: String)