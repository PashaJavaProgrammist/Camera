package com.haretskiy.pavel.magiccamera.ui.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.SIGN_OUT_CODE
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.utils.interfaces.Router
import com.haretskiy.pavel.magiccamera.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject


class SettingsFragment : Fragment() {

    private val router: Router by inject()
    private val prefs: Prefs by inject()
    private val settingsViewModel: SettingsViewModel by inject()

    val data = settingsViewModel.userInfo.observe(this, Observer {
        if (it == SIGN_OUT_CODE) {
            signOut()
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_settings, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settings_progress_bar.visibility = View.GONE
        bt_settings.setOnClickListener({ router.startSettingsActivity() })
        bt_log_out.setOnClickListener({
            settings_progress_bar.visibility = View.VISIBLE
            settingsViewModel.logOut()
        })
    }

    //todo: move this in view model
    private fun signOut() {
        settings_progress_bar.visibility = View.GONE
        prefs.setUserStateLogOut()
        router.startLoginActivity()
    }

}
