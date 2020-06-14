package com.example.electronicshunter.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.example.electronicshunter.R
import kotlinx.android.synthetic.main.fragment_item_details.*
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import kotlinx.android.synthetic.main.item_item.view.*

class ItemDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = Navigation.findNavController(view)
        detailsName.text = arguments?.getString("detailsName")!!
        detailsPrice.text = detailsPrice.text.toString() + arguments?.getString("detailsPrice")!! + "zł"
        detailsMinPrice.text = detailsMinPrice.text.toString() + arguments?.getString("detailsMinPrice")!! + "zł"
        detailsMaxPrice.text = detailsMaxPrice.text.toString() + arguments?.getString("detailsMaxPrice")!! + "zł"
        detailsShopName.text = detailsShopName.text.toString() + arguments?.getString("detailsShopName")!!
        view.detailsName.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(arguments?.getString("detailsHref"))
            startActivity(openURL)
        }
        view.returnButton.setOnClickListener {
            navController.popBackStack()
        }

    }

}
