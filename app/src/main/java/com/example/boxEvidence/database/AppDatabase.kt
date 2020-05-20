package com.example.boxEvidence.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.boxEvidence.database.dao.*
import com.example.boxEvidence.database.model.*

@Database(entities = arrayOf(Box::class, Item::class,Location::class, Category::class,Keyword::class,Photo::class, ItemKeywordCrossRef::class), version = 1)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun boxDAO(): BoxDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun keywordDAO(): KeywordDAO
    abstract fun locationDAO(): LocationDAO
    abstract fun photoDAO(): PhotoDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "box-evidence.db")
            .build()
    }
}
