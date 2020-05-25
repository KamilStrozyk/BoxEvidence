package com.example.boxEvidence.activities.table.main.item

import com.example.boxEvidence.database.model.Item


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
import com.example.boxEvidence.database.model.Photo
import kotlin.ByteArray
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.io.ByteArrayOutputStream


class ItemEdit : AppCompatActivity() {
    var code: String = ""
    var photo: MutableCollection<ByteArray> ? = null
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_edit)
        val db = AppDatabase(this)
        val boxesToUse = db.boxDAO().getAllNames()

        var spinner = this.findViewById<Spinner>(
            R.id.item_boxes
        )
        val spinnerAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            boxesToUse
        )
        spinner.adapter = spinnerAdapter
        this.findViewById<Button>(R.id.item_ean).setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES)
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

        val itemId: Int = intent.getIntExtra("ITEM_ID", -1)
        Log.w("itemId", itemId.toString())
        if (itemId == -1) {
            this.findViewById<Button>(R.id.item_add).setOnClickListener {
                val error = AlertDialog.Builder(this)
                    .setMessage("Invalid data in form, please try again.")
                    .setPositiveButton("OK", null)
                try {
                    val name = this.findViewById<EditText>(R.id.item_name).text.toString()
                    val comment = this.findViewById<EditText>(R.id.item_comment).text.toString()
                    val keywords =
                        this.findViewById<EditText>(R.id.item_keywords).text.toString().split(',')

                    if (name == "" || comment == "") throw Exception()
                    Log.w("CODE", code)


                    val boxId: Int =
                        db.boxDAO().getIdByName(spinner.selectedItem.toString())
                    val itemToAdd = Item(0, name, code, 0, boxId, comment)
                    db.itemDAO().add(itemToAdd)
                    val newItemId = db.itemDAO().getByValues(name, boxId, comment).id

                    if (photo != null) {
                        if (photo!!.size > 0) {
                            db.photoDAO().add(photo!!.map { value -> Photo(0, newItemId, value) })
                        }
                    }
                    setResult(RESULT_OK, null);
                    this.finish()
                } catch (e: Exception) {
                    error.show()
                }
            }

        } else {
            var item = db.itemDAO().getById(itemId)
            val boxitem = item.boxId?.let { db.boxDAO().getById(it) }
            this.findViewById<Button>(R.id.item_ean).text = "Change Code"
            this.findViewById<EditText>(R.id.item_name).setText(item.name)
            this.findViewById<EditText>(R.id.item_comment).setText(item.comment)
            this.findViewById<EditText>(R.id.item_comment).setText(item.comment)

            var keywordZipped = ""
            db.keywordItemDAO().getByItemId(itemId)
                .map { value -> keywordZipped += db.keywordDAO().getById(value.keywordId).name + ',' }

            this.findViewById<EditText>(R.id.item_keywords).setText(keywordZipped)
            code = item.eanCode.toString()
            spinner.setSelection(spinnerAdapter.getPosition(boxitem?.name))
            this.findViewById<Button>(R.id.item_add).text = "Update"
            this.findViewById<Button>(R.id.item_add).setOnClickListener {
                val error = AlertDialog.Builder(this)
                    .setMessage("Invalid data in form, please try again.")
                    .setPositiveButton("OK", null)
                try {
                    val name = this.findViewById<EditText>(R.id.item_name).text.toString()
                    val comment = this.findViewById<EditText>(R.id.item_comment).text.toString()
                    val keywords =
                        this.findViewById<EditText>(R.id.item_keywords).text.toString().split(',')

                    if (name == "" || comment == "") throw Exception()
                    Log.w("CODE", code)


                    if (photo != null) {

                        if (photo!!.isNotEmpty()) {
                            try {
                                db.photoDAO()
                                    .add(photo!!.map { value -> Photo(0, itemId, value) })
                            } catch (exception: Exception) {
                            }
                        }

                    }

                    val boxId: Int =
                        db.boxDAO().getIdByName(spinner.selectedItem.toString())
                    item.name = name
                    item.boxId = boxId
                    item.eanCode = code
                    item.comment = comment

                    db.itemDAO().updateElem(item)
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
                if (photo != null)
                    photo?.add(stream.toByteArray())
                else {

                    (photo as MutableList<ByteArray>).add(stream.toByteArray())

                }
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
