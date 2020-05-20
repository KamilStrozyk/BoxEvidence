package com.example.boxEvidence.database.model

import androidx.room.*

@Entity(tableName = "categories")
data class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String)

data class CategoryWithItems(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userCreatorId"
    )
    val items: List<Item>
)
