package com.example.boxEvidence.activities.table.main.item

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.table.main.item.ItemEdit
import com.example.boxEvidence.database.AppDatabase
import kotlinx.android.synthetic.main.activity_item_details.view.*

class ItemDetails : AppCompatActivity() {
    var itemId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        itemId = intent.getStringExtra("ITEM_ID").toInt()
        val db = AppDatabase(this)
        val item = db.itemDAO().getById(itemId)


        this.findViewById<TextView>(R.id.item_name).text = item.name
        this.findViewById<TextView>(R.id.item_boxes).text =
            item.boxId?.let { "Box:" + db.boxDAO().getById(it).name }
        val commentArray = item.comment.chunked(40)
        var comment : String = ""
        commentArray.forEach { commentLine -> comment += commentLine + '\n' }
        this.findViewById<TextView>(R.id.item_comment).text = comment

if(item.eanCode!=null) {
    this.findViewById<TextView>(R.id.item_details_ean).text = "EAN:" + item.eanCode
}else{
    this.findViewById<TextView>(R.id.item_details_ean).visibility = View.INVISIBLE
}
        this.findViewById<Button>(R.id.box_edit_btn).setOnClickListener{
            val activity2Intent = Intent(
                this,
                ItemEdit::class.java
            )
            activity2Intent.putExtra("ITEM_ID", itemId)
            startActivityForResult(activity2Intent, 1);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            val refresh : Intent = Intent(this, ItemDetails::class.java)
            refresh.putExtra("ITEM_ID", itemId.toString());
            startActivity(refresh)
            this.finish();
        }
    }
}
