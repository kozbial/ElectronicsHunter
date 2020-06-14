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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

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
        val requestOptions: RequestOptions = RequestOptions().transform(FitCenter(), RoundedCorners(16)).override(300, 300)
        Glide.with(activity!!)
            .load(arguments?.getString("detailsImageHref"))
            .placeholder(R.drawable.ic_launcher_background)
            .apply(requestOptions)
            .into(detailsItemImage)
        detailsName.text = arguments?.getString("detailsName")!!
        detailsPriceValue.text = arguments?.getString("detailsPrice")!! + "zł"
        detailsMinPriceValue.text = arguments?.getString("detailsMinPrice")!! + "zł"
        detailsMaxPriceValue.text = arguments?.getString("detailsMaxPrice")!! + "zł"
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
