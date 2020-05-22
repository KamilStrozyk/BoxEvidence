package com.example.boxEvidence.activities.table.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
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
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Item
import com.example.boxEvidence.database.viewmodel.ItemViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_table.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [itemView.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemView : ListFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        val db = this.context?.let { AppDatabase(it) }

        var items = db?.itemDAO()?.getAll()
        val adapter = this.context?.let {
            items?.map{ value ->

                   ItemViewModel(
                            value.name,
                       db?.photoDAO()?.getByItemId(value.id)?.get(0)?.Data?.size?.let { it1 -> BitmapFactory.decodeByteArray(db?.photoDAO()?.getByItemId(value.id)?.get(0)?.Data, 0, it1) })

            }?.let { it1 ->
                ItemAdapter(
                    it,
                    it1
                )
            }
        }

        val list: ListView = listView
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            items = db?.itemDAO()?.getAll()
            val wholeItem = adapter?.getItem(position)
            val item = wholeItem?.name
            val id = items?.filter { value -> value.name.equals(item) }?.single()?.id
            if (id != null) {

                val removeError = AlertDialog.Builder(this.context)
                    .setMessage("Error, please try again.")
                    .setPositiveButton("OK", null)


                AlertDialog.Builder(this.context)
                    .setMessage(item).setPositiveButton("Details", null)
                    .setNeutralButton(
                        "Remove"
                    ) { _, _ ->
                        try {
                            val db = this.context?.let { it1 -> AppDatabase(it1) }
                            if (db != null) {
                                    val toRemove = db.itemDAO().getById(id)
                                    db.itemDAO().remove(toRemove)
                                    adapter?.remove(wholeItem)
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
        fun newInstance(param1: String, param2: String) =
            LocationView().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class ItemAdapter(context: Context?, users: List<ItemViewModel?>?) :
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
                LayoutInflater.from(context).inflate(R.layout.item_item, parent, false)
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