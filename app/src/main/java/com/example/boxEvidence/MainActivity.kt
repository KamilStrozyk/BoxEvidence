package com.example.boxEvidence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Keyword
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {

            val db = AppDatabase(applicationContext)

            db.keywordDAO().add(Keyword(0, "something"))
            Log.w("db", db.keywordDAO().getAll().size.toString())
        } catch (e: Exception){
            Log.w("db",e.toString())
        }
    }
}

