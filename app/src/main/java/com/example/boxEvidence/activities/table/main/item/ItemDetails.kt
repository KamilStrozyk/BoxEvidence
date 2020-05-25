package com.example.boxEvidence.activities.table.main.item

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Photo
import com.example.boxEvidence.database.viewmodel.ItemViewModelWithBox

class ItemDetails : AppCompatActivity() {
    var itemId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)
        itemId = intent.getStringExtra("ITEM_ID").toInt()
        val db = AppDatabase(this)
        val item = db.itemDAO().getById(itemId)

        var keywordZipped = ""
        db.keywordItemDAO().getByItemId(itemId)
            .map { value -> keywordZipped += db.keywordDAO().getById(value.keywordId)!!.name + ',' }

        this.findViewById<TextView>(R.id.item_details_keywords).setText(keywordZipped)

        this.findViewById<TextView>(R.id.item_name).text = item.name
        this.findViewById<TextView>(R.id.item_boxes).text =
            item.boxId?.let { "Box:" + db.boxDAO().getById(it).name }
        val commentArray = item.comment.chunked(40)
        var comment : String = ""
        commentArray.forEach { commentLine -> comment += commentLine + '\n' }
        this.findViewById<TextView>(R.id.item_comment).text = comment

        val adapter =
            PhotoAdapter(this,
                db.photoDAO().getByItemId(item.id).map(Photo::Data))
        val list: ListView = this.findViewById(R.id.list)
        list.adapter = adapter
//        list.setOnItemClickListener { parent, view, position, id ->

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

class PhotoAdapter(context: Context?, users: List<ByteArray>?) :
    ArrayAdapter<ByteArray>(context!!, 0, users!!) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View { // Get the data item for this position
        var convertView = convertView
        val photo: ByteArray? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false)
        }
        // Lookup view for data population
        val imageView = convertView!!.findViewById<ImageView>(R.id.photo_image) as ImageView
        // Populate the data into the template view using the data object
        if (photo != null) {
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(photo,0, photo.size))
        }

        // Return the completed view to render on screen
        return convertView

    }
}
