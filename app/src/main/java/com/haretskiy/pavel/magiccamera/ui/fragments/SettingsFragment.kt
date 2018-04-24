package com.haretskiy.pavel.magiccamera.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.navigation.Router
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    val router: Router by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View= inflater.inflate(R.layout.fragment_settings, container, false)
        view.bt_settings.setOnClickListener({router.startSettingsActivity()})
        return view
    }


}
