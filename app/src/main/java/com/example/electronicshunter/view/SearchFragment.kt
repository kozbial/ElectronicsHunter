package com.example.electronicshunter.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.electronicshunter.R
import kotlinx.android.synthetic.main.fragment_search.*

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
        //setSearchNavBarIconActive()
        button.setOnClickListener{
            if(searchedText.text.toString().isNotEmpty()) {
                var bundle = bundleOf("searchedPhrase" to searchedText.text.toString())
                searchedText.text.clear()
                navController.navigate(R.id.action_searchFragment_to_searchResultsFragment2, bundle)
            }
        }

    }
    /*
    private fun setSearchPageNavBarIconGone(){
        bottom_navigation.menu.removeItem(R.id.searchingPage)
    }
    private fun setSearchNavBarIconActive(){
        bottom_navigation.selectedItemId = R.id.searchingPage
    }

     */
}
