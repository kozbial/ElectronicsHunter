package com.example.electronicshunter.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.electronicshunter.R
import com.example.electronicshunter.data.models.ObservedItemModel
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_observed_items.*
import kotlinx.android.synthetic.main.observed_item_item.view.*

class ObservedItemsFragment : Fragment() {
    lateinit var observedItemRepository: ObservedItemRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    it?.map { item ->
                        ObservedItemModel(
                            item.shopName,
                            item.name,
                            item.price,
                            item.maxPrice,
                            item.minPrice,
                            item.goalPrice,
                            item.href
                        )
                    } ?: ArrayList()
                if(mappedList.isEmpty()){
                    makeNoObservedItemsInfoVisible()
                }
                else{
                    observedItemsAdapter.addItems(mappedList.sortedBy { (it.price?.minus(it.priceGoal)) })
                }

                makeProgressBarGone()
            },{
                makeProgressBarGone()
                makeNoObservedItemsInfoVisible()
            })
        }

    private fun makeNoObservedItemsInfoVisible(){
        if(noObservedItemsInfo != null) noObservedItemsInfo.visibility = View.VISIBLE
    }

    private fun makeProgressBarGone() {
        if(observedItemsProgressBar != null) observedItemsProgressBar.visibility = View.INVISIBLE
    }

    @SuppressLint("SetTextI18n")
    private val observedItemsAdapter = CustomRecyclerViewAdapter(R.layout.observed_item_item,
        onBind = {
                view: View, item: ObservedItemModel, index ->
            view.observedItemName.text = item.name
            if(item.price!! <= item.priceGoal) {
                view.observedItemName.typeface = Typeface.DEFAULT_BOLD
                view.observedItemName.setTextSize(20.0F)
                view.observedItemName.setTextColor(Color.parseColor("#FFBB33"))
                view.observedItemMaxPrice.setTextColor(Color.parseColor("#FFBB33"))
                view.observedItemPrice.setTextColor(Color.parseColor("#FFBB33"))
                view.observedItemMinPrice.setTextColor(Color.parseColor("#FFBB33"))
            }
            view.observedItemPrice.text = "Cena: ${item.price} zł"
            view.observedItemMaxPrice.text = "Cena maksymalna: ${item.maxPrice} zł"
            view.observedItemMinPrice.text = "Cena minimalna: ${item.minPrice} zł"
            view.observedItemPriceGoal.text = "Cena docelowa: ${item.priceGoal} zł"
            view.observedItemName.setOnClickListener{
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(item.href)
                startActivity(openURL)
            }
            view.deleteObservedItemButton.setOnClickListener {
                Single.fromCallable {
                    observedItemRepository.deleteItemByHref(item.href!!)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
                Navigation.findNavController(view).navigate(R.id.action_observedItemsFragment_self)
            }
        })

}
