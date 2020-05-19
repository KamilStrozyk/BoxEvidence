package com.example.boxEvidence.database.model

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val Id: Int,
    @ColumnInfo(name = "entityid")
    val EntityId: Int,
    @ColumnInfo(name = "entityid", typeAffinity = ColumnInfo.BLOB)
    val Data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Photo

        if (Id != other.Id) return false
        if (EntityId != other.EntityId) return false
        if (!Data.contentEquals(other.Data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Id
        result = 31 * result + EntityId
        result = 31 * result + Data.contentHashCode()
        return result
    }
}

