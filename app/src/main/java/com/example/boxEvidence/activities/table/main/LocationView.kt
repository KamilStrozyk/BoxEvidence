package com.example.boxEvidence.activities.table.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.navigation.fragment.findNavController
import androidx.room.Delete
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Location
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_table.*
import kotlinx.android.synthetic.main.fragment_first.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationView.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationView : ListFragment() {
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
        return inflater.inflate(R.layout.fragment_location_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val db = this.context?.let { AppDatabase(it) }

        var locations = db?.locationDAO()?.getAll()
        val adapter = this.context?.let {
            locations?.map { value -> value.name }?.let { it1 ->
                ArrayAdapter<String>(
                    it,
                    R.layout.location_item,
                    R.id.location_name,
                    it1
                )
            }
        }

        val list: ListView = listView
        list.adapter = adapter

        list.setOnItemClickListener { parent, view, position, id ->
            locations = db?.locationDAO()?.getAll()
            val item = adapter?.getItem(position)
            val id = locations?.filter { value -> value.name.equals(item) }?.single()?.id
            if (id != null) {

                val removeError = AlertDialog.Builder(this.context)
                    .setMessage("Error, please try again.")
                    .setPositiveButton("OK", null)
                val nullError = AlertDialog.Builder(this.context)
                    .setMessage("Error, location is not empty.")
                    .setPositiveButton("OK", null)

                AlertDialog.Builder(this.context)
                    .setMessage(item)
                    .setPositiveButton("Boxes", null)
                 .setNeutralButton(
                        "Remove"
                    ) { _, _ ->
                        try {
                            val db = this.context?.let { it1 -> AppDatabase(it1) }
                            if (db != null) {
                                if(!db.boxDAO().getByLocationId(id).isEmpty())
                                {
                                db.locationDAO().remove(Location(id, item.toString()))
                                adapter?.remove(item)
                                }else nullError.show()
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

            val locationToSet = EditText(this.context)

// Set the default text to a link of the Queen
            // Set the default text to a link of the Queen
            locationToSet.hint =
                "Location Name"

            AlertDialog.Builder(this.context)
                .setTitle("Add Location")
                .setView(locationToSet)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    val locationName = locationToSet.text.toString()
                    val error = AlertDialog.Builder(this.context)
                        .setMessage("Invalid data in form, please try again.")
                        .setPositiveButton("OK", null)

                    try {
                        if (locationName == "") throw Exception()

                        val db = this.context?.let { it1 -> AppDatabase(it1) }
                        if (db != null) {
                            var id = db.locationDAO().getAll().map{ value -> value.id }.max()
                                ?.plus(1)
                            id?.let { it1 -> Location(it1, locationName.toString()) }?.let { it2 ->
                                db.locationDAO().add(
                                    it2
                                )
                            }
                            adapter?.add(locationName)
                        } else
                            throw Exception()

                    } catch (exception: Exception) {
                        error.show()
                    }
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, whichButton -> }
                .show()
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
