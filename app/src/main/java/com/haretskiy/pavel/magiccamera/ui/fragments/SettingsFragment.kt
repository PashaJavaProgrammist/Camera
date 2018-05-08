package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.SIGN_OUT_CODE
import com.haretskiy.pavel.magiccamera.viewModels.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

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
        bt_settings.setOnClickListener { settingsViewModel.startGoToSettings() }

        bt_log_out.setOnClickListener {
            settings_progress_bar.visibility = View.VISIBLE
            settingsViewModel.logOut()
        }

        bt_delete_photos.setOnClickListener {
            settingsViewModel.clearPhotosDB(childFragmentManager)
        }

        bt_delete_qrs.setOnClickListener {
            settingsViewModel.clearQrDB(childFragmentManager)
        }

        bt_delete_account.setOnClickListener {
            activity?.let { activity -> settingsViewModel.deleteAccount(activity, childFragmentManager) }
        }
    }

    private fun signOut() {
        settings_progress_bar.visibility = View.GONE
        settingsViewModel.signOut()
    }

}
