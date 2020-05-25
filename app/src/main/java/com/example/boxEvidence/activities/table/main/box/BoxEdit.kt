package com.example.boxEvidence.activities.table.main.box

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Box
import com.example.boxEvidence.database.model.Photo
import kotlin.ByteArray
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.io.ByteArrayOutputStream


class BoxEdit : AppCompatActivity() {
    var code: String = ""
    var photo: ByteArray? = null
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_edit)
        val db = AppDatabase(this)
        val locationsToUse = db.locationDAO().getAllNames()

        var spinner = this.findViewById<Spinner>(
            R.id.item_boxes
        )
        val spinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            locationsToUse
        )
        spinner.adapter = spinnerAdapter
        this.findViewById<Button>(R.id.item_ean).setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            integrator.setPrompt("Scan")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(false)
            integrator.initiateScan()
        }

        this.findViewById<Button>(R.id.item_photo).setOnClickListener {

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }

        }

        val boxId: Int = intent.getIntExtra("BOX_ID", -1)
        Log.w("boxId", boxId.toString())
        if (boxId == -1) {
            this.findViewById<Button>(R.id.item_add).setOnClickListener {
                val error = AlertDialog.Builder(this)
                    .setMessage("Invalid data in form, please try again.")
                    .setPositiveButton("OK", null)
                try {
                    val name = this.findViewById<EditText>(R.id.item_name).text.toString()
                    val comment = this.findViewById<EditText>(R.id.item_comment).text.toString()

                    if (name == "" || comment == "" || code == "") throw Exception()
                    Log.w("CODE", code)
                    var photoId: Int? = null

                    if (photo != null) {
                        db.photoDAO().add(Photo(0, null, photo!!))
                    }


                    val locationId: Int =
                        db.locationDAO().getIdByName(spinner.selectedItem.toString())
                    val boxToAdd = Box(0, name, locationId, comment, code, photoId)
                    db.boxDAO().add(boxToAdd)
                    setResult(RESULT_OK, null);
                    this.finish()
                } catch (e: Exception) {
                    error.show()
                }

            }
        } else {
            var box = db.boxDAO().getById(boxId)
            val boxLocation = db.locationDAO().getById(box.locationId)
            this.findViewById<Button>(R.id.item_ean).text = "Change Code"
            this.findViewById<Button>(R.id.item_photo).text = "Change Photo"
            this.findViewById<EditText>(R.id.item_name).setText(box.name)
            this.findViewById<EditText>(R.id.item_comment).setText(box.comment)
            code = box.code
            spinner.setSelection(spinnerAdapter.getPosition(boxLocation.name))
            this.findViewById<Button>(R.id.item_add).text = "Update"
            this.findViewById<Button>(R.id.item_add).setOnClickListener {
                val error = AlertDialog.Builder(this)
                    .setMessage("Invalid data in form, please try again.")
                    .setPositiveButton("OK", null)
                try {
                    val name = this.findViewById<EditText>(R.id.item_name).text.toString()
                    val comment = this.findViewById<EditText>(R.id.item_comment).text.toString()

                    if (name == "" || comment == "" || code == "") throw Exception()
                    Log.w("CODE", code)
                    var photoId: Int? = null

                    if (photo != null) {
                        db.photoDAO().add(Photo(0, null, photo!!))

                        if (box.photoId != null) {
                            db.photoDAO().remove(db.photoDAO().getById(box.photoId!!))
                        }
                        box.photoId = db.photoDAO().getAll().filter{value -> value.Data.contentEquals(
                            photo!!
                        ) }.single().id
                    }

                    val locationId: Int =
                        db.locationDAO().getIdByName(spinner.selectedItem.toString())
                    box.name = name
                    box.locationId = locationId
                    box.code = code
                    box.comment = comment

                    db.boxDAO().updateElem(box)
                    setResult(RESULT_OK, null);
                    this.finish()
                } catch (e: Exception) {
                    Log.w("error", e.toString())
                    error.show()
                }


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                val bitmap = data?.extras?.get("data") as Bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                photo = stream.toByteArray()
                this.findViewById<Button>(R.id.item_photo).setBackgroundColor(Color.GREEN)
            }
        } else {

            val result: IntentResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    code = result.getContents();
                    this.findViewById<Button>(R.id.item_ean).setBackgroundColor(Color.GREEN)
                }
            } else {
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
