package com.example.boxEvidence.activities.configuration

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.boxEvidence.R
import com.example.boxEvidence.database.AppDatabase
import com.example.boxEvidence.database.model.Location
import kotlinx.android.synthetic.main.fragment_first.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val error = AlertDialog.Builder(this.context)
            .setMessage("Invalid data in form, please try again.")
            .setPositiveButton("OK", null)

        view.findViewById<Button>(R.id.button_next).setOnClickListener {
            try {
                if(name.text.toString() == "") throw Exception()
                Thread {
                    val db = this.context?.let { it1 -> AppDatabase(it1) }
                    if (db != null) {
                        db.locationDAO().add(Location(0, name.text.toString()))
                    } else
                        throw Exception()
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }.start()
            } catch (exception: Exception) {
                    error.show()
            }

        }
    }
}
