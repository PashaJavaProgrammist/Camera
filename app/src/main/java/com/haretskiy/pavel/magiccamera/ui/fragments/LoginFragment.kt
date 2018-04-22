package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.SIGN_IN_FLAG
import com.haretskiy.pavel.magiccamera.SIGN_UP
import com.haretskiy.pavel.magiccamera.models.FirebaseLoginResponse
import com.haretskiy.pavel.magiccamera.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.android.synthetic.main.fragment_sign.view.*
import org.koin.android.ext.android.inject


class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by inject()

    //this flag shows SignIn or SignUp screen
    private var isSignInScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSignInScreen = arguments?.get(BUNDLE_KEY_SIGN)?.equals(SIGN_IN_FLAG) ?: false

        loginViewModel.userInfo.observe(this, Observer<FirebaseLoginResponse> {
            login_progress.visibility = View.GONE
            when (it?.user) {
                null -> Toast.makeText(context, "${it?.errorMessage}", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(context, "Success: ${it.user?.email}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign, container, false)
        if (isSignInScreen) {
            view.repeate_password.visibility = View.GONE
        } else {
            view.repeate_password.visibility = View.VISIBLE
            view.email_sign_in_button.text = SIGN_UP
        }
        view.email_sign_in_button.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        when (v) {
            email_sign_in_button -> sign()
        }
    }

    private fun sign() {
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        if (!emailStr.isEmpty() && !passwordStr.isEmpty()) {
            login_progress.visibility = View.VISIBLE
            when (isSignInScreen) {
                true -> loginViewModel.signIn(emailStr, passwordStr)
                false -> {
                    val repeatPasswordStr = repeate_password.text.toString()
                    if (passwordStr == repeatPasswordStr) {
                        loginViewModel.signUp(emailStr, passwordStr)
                    } else {
                        Toast.makeText(context, "Passwords doesn't match", Toast.LENGTH_SHORT).show()
                        login_progress.visibility = View.GONE
                    }
                }
            }
        } else {
            Toast.makeText(context, "Password and email fields mustn't be empty", Toast.LENGTH_SHORT).show()
            login_progress.visibility = View.GONE
        }
    }


}
