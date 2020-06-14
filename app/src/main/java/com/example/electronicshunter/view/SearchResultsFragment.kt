package com.example.electronicshunter.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.electronicshunter.R
import com.example.electronicshunter.data.models.ItemModel
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import com.example.electronicshunter.remote.BackendService
import com.example.electronicshunter.remote.RetrofitClient
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.item_item.view.*

class SearchResultsFragment : Fragment() {
    lateinit var observedItemRepository: ObservedItemRepository
    lateinit var isItemObserved: Single<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observedItemRepository = ObservedItemRepository(parentFragment!!.activity!!.applicationContext)
        if(arguments?.getString("searchedPhrase")!=null) {
            val searchPhrase: String = arguments?.getString("searchedPhrase")!!
            System.out.println(searchPhrase)
            rvItems.adapter = itemsAdapter
            val client = RetrofitClient().provideRetrofit().create(BackendService::class.java)
            client.getItemsByName(searchPhrase)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val mappedList: List<ItemModel> =
                        it?.map { item ->
                            ItemModel(
                                item.shopName ?: "",
                                item.name ?: "",
                                item.price ?: 0.0,
                                item.max_price ?: 0.0,
                                item.min_price ?: 0.0,
                                item.href ?: "",
                                item.image_href ?: ""
                            )
                        }
                            ?: ArrayList()
                    itemsAdapter.addItems(mappedList)
                    makeProgressBarGone()
                }, {
                    makeProgressBarGone()
                    makeNotFoundErrorVisible()
                    it.printStackTrace()
                })
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        itemsAdapter.clearItems()
    }

    private fun makeNotFoundErrorVisible(){
        if(notFoundError != null) notFoundError.visibility = View.VISIBLE
    }

    private fun makeProgressBarGone(){
        if(progressBar != null) progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private val itemsAdapter = CustomRecyclerViewAdapter(R.layout.item_item,
        onBind = {
                view: View, item: ItemModel, index ->
            view.txtName.text = item.name
            view.txtPrice.text = "${item.price} zł"
            view.setOnClickListener{
                val navController: NavController = Navigation.findNavController(view)
                val bundle = bundleOf("detailsName" to item.name, "detailsShopName" to item.shopName, "detailsPrice" to item.price.toString(),
                    "detailsMinPrice" to item.minPrice.toString(), "detailsMaxPrice" to item.maxPrice.toString(), "detailsHref" to item.href, "detailsImageHref" to item.imageHref)
                navController.navigate(R.id.action_searchResultsFragment_to_itemDetailsFragment, bundle)
            }

            view.observeItemButton.setOnClickListener {
                val navController: NavController = Navigation.findNavController(view)
                Single.fromCallable {
                    observedItemRepository.isItemObserved(item.href!!)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // disable search button if item is observed
                        if(it == 1){
                            navController.navigate(R.id.itemObservedDialog)
                        }
                        else{
                            val bundle = bundleOf("itemName" to item.name, "itemShopName" to item.shopName, "itemPrice" to item.price.toString(),
                                "itemMinPrice" to item.minPrice.toString(), "itemMaxPrice" to item.maxPrice.toString(), "itemHref" to item.href)
                            navController.navigate(R.id.action_searchResultsFragment_to_observeItemFragment, bundle)
                        }
                    },{
                        it.printStackTrace()
                    })

            }
        })
}
