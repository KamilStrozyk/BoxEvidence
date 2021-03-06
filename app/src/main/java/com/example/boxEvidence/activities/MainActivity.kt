package com.example.boxEvidence.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.configuration.AddLocation
import com.example.boxEvidence.activities.table.main.TableActivity
import com.example.boxEvidence.database.AppDatabase
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED ||
            ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                IntentIntegrator.REQUEST_CODE
            );
        }
        val db = AppDatabase(applicationContext)

        Thread {
            val isConfigured = db.locationDAO().getAll().isNotEmpty()
            if (isConfigured) {
//                    val look = db.locationDAO().getAll()
//                    for( item in look){
//                    db.locationDAO().remove(item)
//                    }
                val activity2Intent = Intent(
                    applicationContext,
                    TableActivity::class.java
                )
                startActivity(activity2Intent)
                this.finish()
            } else {
                configurationButton.setOnClickListener {
                    val activity2Intent = Intent(
                        applicationContext,
                        AddLocation::class.java
                    )
                    startActivity(activity2Intent)
                }
            }
        }.start()
    }

    //    fun onCreateOptionsMenu(
//        menu: ContextMenu?,
//        v: View?,
//        menuInfo: ContextMenu.ContextMenuInfo?
//    ) {
//        super.onCreateContextMenu(menu, v, menuInfo)
//        val inflater = menuInflater
//        inflater.inflate(R.layout.qr_menu, menu)
//    }
}


