package com.example.boxEvidence.activities.table.main.item

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import com.example.boxEvidence.R
import com.example.boxEvidence.activities.table.main.TableActivity
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Item
import com.example.boxEvidence.database.viewmodel.ItemViewModelWithBox
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_table.fab
import kotlinx.android.synthetic.main.fragment_item_view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val BOX_ID = "locationId"
private const val CODE = "code"


/**
 * A simple [Fragment] subclass.
 * Use the [itemView.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemView : ListFragment() {
    // TODO: Rename and change types of parameters
    public var boxId: Int = -1
    public var code: String = "-1"

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            boxId = it.getInt(BOX_ID)
            code = it.getString(CODE).toString()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        readBundle(arguments);
        val db = this.context?.let { AppDatabase(it) }

        var items: Array<Item>? = null
        if (boxId != -1) items = db?.itemDAO()?.getByBoxId(boxId)
        else if (code != null && code != "-1") items = db?.itemDAO()?.getByCode(code)
        else items = db?.itemDAO()?.getAll()

        val adapter =
            ItemAdapter(this.context,
                items?.map { value ->
                    value.boxId?.let { db?.boxDAO()?.getById(it)?.name }?.let {
                        ItemViewModelWithBox(
                            value.name,
                            it
                        )
                    }

                })

        val list: ListView = listView
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            if (boxId != -1) items = db?.itemDAO()?.getByBoxId(boxId)
            else if (code != null && code != "-1") items = db?.itemDAO()?.getByCode(code)
            else items = db?.itemDAO()?.getAll()

            val wholeItem = adapter?.getItem(position)
            val item = wholeItem?.name
            val id = items?.filter { value -> value.name.equals(item) }?.single()?.id
            if (id != null) {

                val removeError = AlertDialog.Builder(this.context)
                    .setMessage("Error, please try again.")
                    .setPositiveButton("OK", null)


                AlertDialog.Builder(this.context)
                    .setMessage(item).setPositiveButton("Details") { _: DialogInterface, _: Int ->

                        val activity2Intent = Intent(
                            this.context,
                            ItemDetails::class.java
                        )
                        activity2Intent.putExtra("ITEM_ID", id.toString());
                        startActivity(activity2Intent)
                    }
                    .setNeutralButton(
                        "Remove"
                    ) { _, _ ->
                        try {
                            val db = this.context?.let { it1 -> AppDatabase(it1) }
                            if (db != null) {
                                val toRemove = db.itemDAO().getById(id)
                                db.itemDAO().remove(toRemove)
                                adapter?.remove(wholeItem)
                                db.photoDAO().remove(db.photoDAO().getByItemId(toRemove.id))

                            } else
                                throw Exception()

                        } catch (exception: Exception) {
                            removeError.show()
                        }
                    }.setNegativeButton("Cancel", null).show()
            }
        }
        val fab: FloatingActionButton = this.fab
        fab.setOnClickListener {
            val activity2Intent = Intent(
                this.context,
                ItemEdit::class.java
            )
            startActivityForResult(activity2Intent, 1);
        }
        if (boxId != -1) {
            this.search.hide()
        } else {
            this.search.setOnClickListener {
                val searchText = EditText(this.context)


                searchText.hint =
                    "Name or keyword"

                val error = AlertDialog.Builder(this.context)
                    .setMessage("Error, please try again.")
                    .setPositiveButton("OK", null)

                AlertDialog.Builder(this.context)
                    .setMessage("Search").setView(searchText)
                    .setPositiveButton("Search") { _: DialogInterface, _: Int ->


                    }
                    .setNeutralButton(
                        "Scan EAN"
                    ) { _, _ ->

                    }.setNegativeButton("Cancel") { _, _ ->

                    }.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            val refresh: Intent = Intent(this.context, TableActivity::class.java)
            refresh.putExtra("BOX_ID", id.toString());
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
            ItemView().apply {
                arguments = Bundle().apply {
                    putInt(BOX_ID, param1)
                    putString(CODE, param2)

                }
            }
    }
}

class ItemAdapter(context: Context?, users: List<ItemViewModelWithBox?>?) :
    ArrayAdapter<ItemViewModelWithBox?>(context!!, 0, users!!) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View { // Get the data item for this position
        var convertView = convertView
        val itemViewModel: ItemViewModelWithBox? = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.item_item, parent, false)
        }
        // Lookup view for data population
        val name = convertView!!.findViewById<View>(R.id.item_name) as TextView
        val box = convertView!!.findViewById<View>(R.id.item_box) as TextView
        // Populate the data into the template view using the data object
        name.text = itemViewModel?.name
        box.text = itemViewModel?.box
        // Return the completed view to render on screen
        return convertView

    }
}