package com.example.boxEvidence.activities.table.main.box

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.ItemActivity
import com.example.boxEvidence.activities.table.main.TableActivity
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Box
import com.example.boxEvidence.database.viewmodel.ItemViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_table.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val LOCATION_ID = "locationId"
private const val CODE = "code"


/**
 * A simple [Fragment] subclass.
 * Use the [itemView.newInstance] factory method to
 * create an instance of this fragment.
 */
class BoxView : ListFragment() {
    // TODO: Rename and change types of parameters
    public var locationId: Int = -1
    public var code: String = "-1"

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            locationId = it.getInt(LOCATION_ID)
            code = it.getString(CODE).toString()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_box_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        readBundle(arguments);
        val db = this.context?.let { AppDatabase(it) }

        var boxes: Array<Box>? = null
        if (locationId != -1) boxes = db?.boxDAO()?.getByLocationId(locationId)
        else if (code != null && code != "-1") boxes = db?.boxDAO()?.getByCode(code)

        val adapter = this.context?.let {
            boxes?.map { value ->

                ItemViewModel(
                    value.name,

                    value.photoId?.let { it1 ->
                        db?.photoDAO()?.getById(it1)?.Data?.size
                    }?.let { it2 ->
                        BitmapFactory.decodeByteArray(
                            value.photoId?.let { it1 ->
                                db?.photoDAO()?.getById(
                                    it1
                                )?.Data
                            }, 0,
                            it2
                        )
                    })
            }?.let { it1 ->
                BoxAdapter(
                    it,
                    it1
                )
            }
        }

        val list: ListView = listView
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            if(locationId != -1)
            boxes = db?.boxDAO()?.getByLocationId(locationId)
            else
                boxes = db?.boxDAO()?.getByCode(code)

            val wholeItem = adapter?.getItem(position)
            val item = wholeItem?.name
            val id = boxes?.filter { value -> value.name.equals(item) }?.single()?.id
            if (id != null) {

                val removeError = AlertDialog.Builder(this.context)
                    .setMessage("Error, please try again.")
                    .setPositiveButton("OK", null)

                if (item != null) {
                    if(item.contains("General space")) {
                        AlertDialog.Builder(this.context)
                            .setMessage(item)
                            .setNegativeButton("Cancel",null).setPositiveButton("Items") { _: DialogInterface, _: Int ->

                                val activity2Intent = Intent(
                                    this.context,
                                    ItemActivity::class.java
                                )
                                activity2Intent.putExtra("BOX_ID", id.toString());
                                startActivity(activity2Intent)
                            }.show()
                    }else {
                        AlertDialog.Builder(this.context)
                            .setMessage(item)
                            .setPositiveButton("Details") { _: DialogInterface, _: Int ->

                                val activity2Intent = Intent(
                                    this.context,
                                    Box_details::class.java
                                )
                                activity2Intent.putExtra("BOX_ID", id.toString());
                                startActivity(activity2Intent)
                            }
                            .setNeutralButton(
                                "Remove"
                            ) { _, _ ->
                                try {
                                    val db = this.context?.let { it1 -> AppDatabase(it1) }
                                    if (db != null) {
                                        if (db.itemDAO().getAll().any { value -> value.boxId == id }) {
                                            val nullError = AlertDialog.Builder(this.context)
                                                .setMessage("Error, box is not empty.")
                                                .setPositiveButton("OK", null).show()
                                        } else {
                                            val toRemove = db.boxDAO().getById(id)
                                            db.boxDAO().remove(toRemove)
                                            adapter?.remove(wholeItem)
                                        }
                                    } else
                                        throw Exception()

                                } catch (exception: Exception) {
                                    removeError.show()
                                }
                            }.setNegativeButton("Items") { _: DialogInterface, _: Int ->

                                val activity2Intent = Intent(
                                    this.context,
                                    ItemActivity::class.java
                                )
                                activity2Intent.putExtra("BOX_ID", id.toString());
                                startActivity(activity2Intent)
                            }.show()
                    }
                }
            }
        }
        val fab: FloatingActionButton = this.fab
        fab.setOnClickListener {
            val activity2Intent = Intent(
                this.context,
                BoxEdit::class.java
            )
            startActivityForResult(activity2Intent, 1);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            val refresh: Intent = Intent(this.context, TableActivity::class.java)
            refresh.putExtra("LOCATION_ID", id.toString());
            startActivity(refresh)
            this.activity?.finish();
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationView.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            BoxView().apply {
                arguments = Bundle().apply {
                    putInt(LOCATION_ID, param1)
                    putString(CODE, param2)

                }
            }
    }
}

class BoxAdapter(context: Context?, users: List<ItemViewModel?>?) :
    ArrayAdapter<ItemViewModel?>(context!!, 0, users!!) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View { // Get the data item for this position
        var convertView = convertView
        val itemViewModel: ItemViewModel? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.box_item, parent, false)
        }
        // Lookup view for data population
        val name = convertView!!.findViewById<View>(R.id.item_name) as TextView
        val image = convertView.findViewById<View>(R.id.item_image) as ImageView
        // Populate the data into the template view using the data object
        name.text = itemViewModel?.name
        image.setImageBitmap(itemViewModel?.photo)
        // Return the completed view to render on screen
        return convertView

    }
}