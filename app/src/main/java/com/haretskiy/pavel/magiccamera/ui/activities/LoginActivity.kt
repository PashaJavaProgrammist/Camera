package com.haretskiy.pavel.magiccamera.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.adapters.TabViewPagerAdapter
import com.haretskiy.pavel.magiccamera.ui.fragments.LoginFragment
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject


class LoginActivity : AppCompatActivity() {

    private val tabViewPagerAdapter = TabViewPagerAdapter(supportFragmentManager)

    private val signInFragment: LoginFragment by inject { mapOf(BUNDLE_KEY_SIGN to SIGN_IN_FLAG) }
    private val signUpFragment: LoginFragment by inject { mapOf(BUNDLE_KEY_SIGN to SIGN_UP_FLAG) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViewPagerAdapter()
    }

    private fun initViewPagerAdapter() {
        tabViewPagerAdapter.addFragment(signUpFragment, SIGN_UP)
        tabViewPagerAdapter.addFragment(signInFragment, SIGN_IN)
        pager.adapter = tabViewPagerAdapter
        tabs.setupWithViewPager(pager)
    }
}