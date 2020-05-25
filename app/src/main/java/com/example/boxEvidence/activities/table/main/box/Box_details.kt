package com.example.boxEvidence.activities.table.main.box

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase

class Box_details : AppCompatActivity() {
var boxId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_details)
         boxId = intent.getStringExtra("BOX_ID").toInt()
        val db = AppDatabase(this)
        val box = db.boxDAO().getById(boxId)

        if(box.photoId != null) {
            val photoArray = db.photoDAO().getById(box.photoId!!).Data
            this.findViewById<ImageView>(R.id.box_image).setImageBitmap(BitmapFactory.decodeByteArray(photoArray,0, photoArray.size))
        }
        this.findViewById<TextView>(R.id.item_name).text = box.name
        this.findViewById<TextView>(R.id.item_boxes).text = db.locationDAO().getById(box.locationId).name
        val commentArray = box.comment.chunked(40)
        var comment : String = ""
    commentArray.forEach { commentLine -> comment += commentLine + '\n' }
        this.findViewById<TextView>(R.id.item_comment).text = comment

            this.findViewById<Button>(R.id.box_edit_btn).setOnClickListener{
                val activity2Intent = Intent(
                    this,
                    BoxEdit::class.java
                )
                activity2Intent.putExtra("BOX_ID", boxId)
                startActivityForResult(activity2Intent, 1);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            val refresh : Intent = Intent(this, Box_details::class.java)
            refresh.putExtra("BOX_ID", boxId.toString());
            startActivity(refresh)
            this.finish();
        }
    }
}
