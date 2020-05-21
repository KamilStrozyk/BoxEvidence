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


            val db = AppDatabase(applicationContext)
        try {
            Thread {
                //Do your database´s operations here

                db.keywordDAO().add(Keyword(2, "somerthing"))
                Log.w("db", db.keywordDAO().getAll().size.toString())
            }.start()
        } catch (e: Exception){
            Log.w("db",e.toString())
        }
    }

}

