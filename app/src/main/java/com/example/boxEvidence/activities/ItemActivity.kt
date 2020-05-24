package com.example.boxEvidence.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.view.menu.MenuView
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.table.main.ItemView
import com.example.boxEvidence.activities.table.main.box.BoxView

class ItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val boxIdStr = intent.getStringExtra("BOX_ID")
        var boxId = -1
        if (boxIdStr != null) boxId = boxIdStr.toInt()
        var code = intent.getStringExtra("CODE")
        if (code == null) code = "-1"
        Log.w("CODE",code)
        val fragment = ItemView.newInstance(boxId, code)
        fragmentTransaction.add(R.id.item_container, fragment)
        fragmentTransaction.commit()

        setContentView(R.layout.activity_item_box)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }
}
