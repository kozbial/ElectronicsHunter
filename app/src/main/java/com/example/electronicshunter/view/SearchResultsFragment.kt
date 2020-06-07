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
import com.example.electronicshunter.R
import com.example.electronicshunter.remote.BackendService
import com.example.electronicshunter.remote.RetrofitClient
import com.example.electronicshunter.utils.CustomRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

        rvItems.adapter = itemsAdapter
        val client = RetrofitClient().provideRetrofit().create(BackendService::class.java)
        client.getItemsByName("iphone xs")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val mappedList: List<ItemModel> =
                    it?.map { item -> ItemModel(item.name ?: "", item.price ?: 0.0, item.href ?: "") } ?: ArrayList()
                itemsAdapter.addItems(mappedList)
                makeProgressBarGone()
            }, {
                makeProgressBarGone()
                it.printStackTrace()
            })

    }
    private fun makeProgressBarGone(){
        progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private val itemsAdapter = CustomRecyclerViewAdapter(R.layout.item_item,
        onBind = {
                view: View, item: ItemModel, index ->

            view.txtName.text = item.name
            view.txtPrice.text = "Cena: ${item.price} zł"
            view.txtName.setOnClickListener{
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(item.href)
                startActivity(openURL)
            }
        })
}
