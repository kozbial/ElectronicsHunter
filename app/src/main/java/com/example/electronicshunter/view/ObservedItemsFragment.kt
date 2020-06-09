package com.example.electronicshunter.view

import android.annotation.SuppressLint
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
import com.example.electronicshunter.data.entities.ObservedItem
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_item_details.view.*
import kotlinx.android.synthetic.main.fragment_observed_items.*
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.item_item.view.*
import kotlinx.android.synthetic.main.item_item.view.observeItemButton
import kotlinx.android.synthetic.main.item_item.view.txtName
import kotlinx.android.synthetic.main.item_item.view.txtPrice
import kotlinx.android.synthetic.main.observed_item_item.view.*

/**
 * A simple [Fragment] subclass.
 */
class ObservedItemsFragment : Fragment() {
    lateinit var observedItemRepository: ObservedItemRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_observed_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvObservedItems.adapter = observedItemsAdapter
        observedItemRepository = ObservedItemRepository(parentFragment!!.activity!!.applicationContext)
        observedItemRepository.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val mappedList: List<ObservedItemModel> =
                    it?.map { item -> ObservedItemModel(item.shopName, item.name, item.price, item.maxPrice, item.minPrice, item.goalPrice, item.href) } ?: ArrayList()
                observedItemsAdapter.addItems(mappedList)
                makeProgressBarGone()
            },{
                makeProgressBarGone()
                makeNotFoundErrorVisible()
            })
        }


    private fun makeNotFoundErrorVisible(){

        observedItemsNotFoundError.visibility = View.VISIBLE
    }
    private fun makeProgressBarGone() {
        observedItemsProgressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private val observedItemsAdapter = CustomRecyclerViewAdapter(R.layout.observed_item_item,
        onBind = {
                view: View, item: ObservedItemModel, index ->
            view.observedItemName.text = item.name
            view.observedItemPrice.text = "Cena: ${item.price} zł"
            view.observedItemMaxPrice.text = "Cena maksymalna ${item.maxPrice}"
            view.observedItemMinPrice.text = "Cena minimalna ${item.minPrice}"
            view.observedItemPriceGoal.text = "Cena docelowa ${item.priceGoal}"
            view.observedItemName.setOnClickListener{
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(item.href)
                startActivity(openURL)
            }
        })
}
