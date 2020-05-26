package com.example.boxEvidence.database

import android.content.Context
import android.os.Environment
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.boxEvidence.database.dao.*
import com.example.boxEvidence.database.model.*
import java.io.File
import java.io.FileOutputStream

@Database(entities = arrayOf(Box::class, Item::class,Location::class, Category::class,Keyword::class,Photo::class, ItemKeywordCrossRef::class), version = 1)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun boxDAO(): BoxDAO
    abstract fun itemDAO(): ItemDAO
    abstract fun categoryDAO(): CategoryDAO
    abstract fun keywordDAO(): KeywordDAO
    abstract fun locationDAO(): LocationDAO
    abstract fun photoDAO(): PhotoDAO
    abstract fun keywordItemDAO(): KeywordItemDAO

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "box-evidence.db").allowMainThreadQueries()
            .build()
    }

    fun exportDatabaseFile(context: Context) {
        try {
            copyDataFromOneToAnother(context.getDatabasePath("box-evidence.db").path, Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db")
            copyDataFromOneToAnother(context.getDatabasePath("box-evidence.db" + "-shm").path, Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db" + "-shm")
            copyDataFromOneToAnother(context.getDatabasePath("box-evidence.db" + "-wal").path, Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db" + "-wal")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun importDatabaseFile(context: Context) {
        try {
            copyDataFromOneToAnother(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db", context.getDatabasePath("box-evidence.db").path)
            copyDataFromOneToAnother(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db" + "-shm", context.getDatabasePath("box-evidence.db" + "-shm").path)
            copyDataFromOneToAnother(Environment.getExternalStorageDirectory().path + "/Download/" + "backup_" + "box-evidence.db" + "-wal", context.getDatabasePath("box-evidence.db" + "-wal").path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyDataFromOneToAnother(fromPath: String, toPath: String) {
        val inStream = File(fromPath).inputStream()
        val outStream = FileOutputStream(toPath)

        inStream.use { input ->
            outStream.use { output ->
                input.copyTo(output)
            }
        }
    }


}
