package com.example.electronicshunter.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.electronicshunter.R

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = Navigation.findNavController(view)
        val button: Button = view.findViewById(R.id.searchButton)
        button.setOnClickListener{
            val searchText: EditText = view.findViewById(R.id.searchedText)
            val searchPhrase: String = searchText.text.toString()
            System.out.println(searchPhrase)
            navController.navigate(R.id.searchResultsFragment)

        }
    }

}
