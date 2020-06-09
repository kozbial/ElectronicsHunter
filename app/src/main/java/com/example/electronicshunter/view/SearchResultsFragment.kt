package com.example.electronicshunter.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.electronicshunter.R
import com.example.electronicshunter.remote.BackendService
import com.example.electronicshunter.remote.RetrofitClient
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.item_item.view.*

/**
 * A simple [Fragment] subclass.
 */
class SearchResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                        it?.map { item -> ItemModel(item.shopName ?: "", item.name ?: "", item.price ?: 0.0, item.max_price ?: 0.0, item.min_price ?: 0.0, item.href ?: "") }
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
        notFoundError.visibility = View.VISIBLE
    }
    private fun makeProgressBarGone(){
        progressBar.visibility = View.GONE
    }

    private val itemsAdapter = CustomRecyclerViewAdapter(R.layout.item_item,
        onBind = {
                view: View, item: ItemModel, index ->

            view.txtName.text = item.name
            view.txtPrice.text = "Cena: ${item.price} zł"
            view.setOnClickListener{
                val navController: NavController = Navigation.findNavController(view)
                val bundle = bundleOf("detailsName" to item.name, "detailsShopName" to item.shopName, "detailsPrice" to item.price.toString(),
                    "detailsMinPrice" to item.minPrice.toString(), "detailsMaxPrice" to item.maxPrice.toString(), "detailsHref" to item.href)
                navController.navigate(R.id.action_searchResultsFragment_to_itemDetailsFragment, bundle)
            }
            view.observeItemButton.setOnClickListener {
                val navController: NavController = Navigation.findNavController(view)
                val bundle = bundleOf("itemName" to item.name, "itemShopName" to item.shopName, "itemPrice" to item.price.toString(),
                    "itemMinPrice" to item.minPrice.toString(), "itemMaxPrice" to item.maxPrice.toString(), "itemHref" to item.href)
                navController.navigate(R.id.action_searchResultsFragment_to_observeItemFragment, bundle)
            }
        })
}
