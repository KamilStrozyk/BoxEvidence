package com.example.boxEvidence.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.Relation

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "itemid")
    val itemId: Int?,
    @ColumnInfo(name = "entityid", typeAffinity = ColumnInfo.BLOB)
    val Data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (id != other.id) return false
        if (itemId != other.itemId) return false
        if (!Data.contentEquals(other.Data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + Data.contentHashCode()
        return result
    }
}

data class BoxWithPhoto(
    @Embedded val box: Box,
    @Relation(
        parentColumn = "id",
        entityColumn = "photoid"
    )
    val photo: Photo?
)

data class ItemWithPhoto(
    @Embedded val item: Item,
    @Relation(
        parentColumn = "id",
        entityColumn = "itemid"
    )
    val photos: List<Photo>
)
