package com.example.boxEvidence.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.configuration.AddLocation
import com.example.boxEvidence.database.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            val db = AppDatabase(applicationContext)

            Thread {
               val isConfigured = db.locationDAO().getAll().isNotEmpty()

                if(isConfigured) {
                    val look = db.locationDAO().getAll()
                    for( item in look){
                    db.locationDAO().remove(item)
                    }
//                    val activity2Intent = Intent(
//                        applicationContext,
//                        TableActivity::class.java
//                    )
//                    startActivity(activity2Intent)
                }else{
                    configurationButton.setOnClickListener{
                        val activity2Intent = Intent(
                            applicationContext,
                            AddLocation::class.java
                        )
                        startActivity(activity2Intent)
                    }
                }
            }.start()
        }
    }


