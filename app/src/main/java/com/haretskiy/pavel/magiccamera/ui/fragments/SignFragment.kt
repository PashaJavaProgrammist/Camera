package com.haretskiy.pavel.magiccamera.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.SIGN_IN_FLAG
import com.haretskiy.pavel.magiccamera.SIGN_UP
import kotlinx.android.synthetic.main.fragment_sign.view.*


class SignFragment : Fragment() {

    var isSignIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSignIn = arguments?.get(BUNDLE_KEY_SIGN)?.equals(SIGN_IN_FLAG) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign, container, false)
        if (isSignIn) {
            view.repeate_password.visibility = View.GONE
        } else {
            view.repeate_password.visibility = View.VISIBLE
            view.email_sign_in_button.text = SIGN_UP
        }
        return view
    }

}
