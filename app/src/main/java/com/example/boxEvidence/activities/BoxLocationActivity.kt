package com.example.boxEvidence.activities

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.table.main.box.BoxView


class BoxLocationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val locationId = intent.getStringExtra("LOCATION_ID").toInt()
        val fragment = BoxView.newInstance(locationId)
        fragmentTransaction.add(R.id.box_container, fragment)
        fragmentTransaction.commit()

        setContentView(R.layout.activity_box_location)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }
}
