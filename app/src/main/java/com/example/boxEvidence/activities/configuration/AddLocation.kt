package com.example.boxEvidence.activities.configuration

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.boxEvidence.R

import kotlinx.android.synthetic.main.activity_add_location.*

class AddLocation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        setSupportActionBar(toolbar)
        title = "Setup an app";
    }

}
