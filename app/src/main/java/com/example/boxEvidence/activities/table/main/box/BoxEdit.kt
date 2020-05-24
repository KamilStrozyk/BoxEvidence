package com.example.boxEvidence.activities.table.main.box

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Box
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class BoxEdit : AppCompatActivity() {
    var code: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_edit)
        var photo: ByteArray? = null
        val db = AppDatabase(this)
        val locationsToUse = db.locationDAO().getAllNames()

        val spinner = this.findViewById<Spinner>(
            R.id.box_location
        )
        spinner.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            locationsToUse
        )
        this.findViewById<Button>(R.id.box_qr).setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

        this.findViewById<Button>(R.id.box_add).setOnClickListener {
            val error = AlertDialog.Builder(this)
                .setMessage("Invalid data in form, please try again.")
                .setPositiveButton("OK", null)
            try {
                val name = this.findViewById<EditText>(R.id.box_name).text.toString()
                val comment = this.findViewById<EditText>(R.id.box_comment).text.toString()

                if (name == "" || comment == "" || code == "") throw Exception()
                Log.w("CODE", code)
                var photoId: Int? = null

                var boxId = db.boxDAO().getAll().map { value -> value.id }.max()?.plus(1)
                if (boxId == null) boxId = 0
                val locationId: Int = db.locationDAO().getIdByName(spinner.selectedItem.toString())
                val boxToAdd = Box(boxId, name, locationId, comment, code, photoId)
                db.boxDAO().add(boxToAdd)
                this.finish()
            } catch (e: Exception) {
                error.show()
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                code = result.getContents();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
