package com.example.electronicshunter.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.electronicshunter.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = activity?.getSharedPreferences("notificationsPreferences", 0)
        val editor = sharedPreferences?.edit()
        notificationsSwitch.isChecked = sharedPreferences?.getBoolean("notificationsSwitch", true)!!
        notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                editor?.putBoolean("notificationsSwitch", true)?.apply()
            }
            else{
                editor?.putBoolean("notificationsSwitch", false)?.apply()
            }
        }
    }
}
