package com.example.electronicshunter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.electronicshunter.R
import com.example.electronicshunter.data.entities.ObservedItem
import com.example.electronicshunter.data.repositories.ObservedItemRepository
import kotlinx.android.synthetic.main.fragment_observe_item.*
import kotlinx.android.synthetic.main.fragment_observe_item.view.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class ObserveItemFragment : Fragment() {
    lateinit var observedItemRepository: ObservedItemRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_observe_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = Navigation.findNavController(view)
        val button: Button = view.findViewById(R.id.observeItemButton)
        //setSearchNavBarIconActive()
        itemName.text = arguments?.getString("itemName")!!
        itemPrice.text = itemPrice.text.toString() + arguments?.getString("itemPrice")!! + "zł"
        itemMinPrice.text = itemMinPrice.text.toString() + arguments?.getString("itemMinPrice")!! + "zł"
        itemMaxPrice.text = itemMaxPrice.text.toString() + arguments?.getString("itemMaxPrice")!! + "zł"
        itemShopName.text = itemShopName.text.toString() + arguments?.getString("itemShopName")!!
        observedItemRepository = ObservedItemRepository(parentFragment!!.activity!!.applicationContext)
        button.setOnClickListener{
            if(priceGoal.text.toString().isNotEmpty()) {
                val observedItem = ObservedItem(arguments?.getString("itemName")!!, arguments?.getString("itemShopName")!!,
                    extractPriceFromString(arguments?.getString("itemPrice")!!), extractPriceFromString(arguments?.getString("itemMinPrice")!!),
                    extractPriceFromString(arguments?.getString("itemMaxPrice")!!), arguments?.getString("itemHref")!!, extractPriceFromString(priceGoal.text.toString()))
                observedItemRepository.save(observedItem)
                navController.navigate(R.id.action_observeItemFragment_to_observedItemsFragment)
            }
        }
        view.returnButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun extractPriceFromString(input: String): Double {
        val p = Pattern.compile("-?\\d+(,\\d+)*?\\.?\\d+?")
        var number = 0.0
        val m: Matcher = p.matcher(input)
        while (m.find()) {
            number = m.group().toDouble()
        }
        return number
    }

}
