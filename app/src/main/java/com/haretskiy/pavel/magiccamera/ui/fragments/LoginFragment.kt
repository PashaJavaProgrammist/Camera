package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.models.FirebaseLoginResponse
import com.haretskiy.pavel.magiccamera.navigation.Router
import com.haretskiy.pavel.magiccamera.utils.Prefs
import com.haretskiy.pavel.magiccamera.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.android.synthetic.main.fragment_sign.view.*
import org.koin.android.ext.android.inject

class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by inject()
    private val router: Router by inject()
    private val prefs: Prefs by inject()

    //this flag shows SignIn or SignUp screen
    private var isSignInScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSignInScreen = arguments?.get(BUNDLE_KEY_SIGN)?.equals(SIGN_IN_FLAG) ?: false

        loginViewModel.userInfo.observe(this, Observer<FirebaseLoginResponse> {
            login_progress.visibility = View.GONE
            when (it?.user) {
                null -> Toast.makeText(context, "${it?.errorMessage}", Toast.LENGTH_LONG).show()
                else -> onSuccessAuth(it.user)
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
        login_progress.visibility = View.VISIBLE
        loginViewModel.sign(email.text.toString(), password.text.toString(), if (!isSignInScreen) repeate_password.text.toString() else EMPTY_STRING)
    }

    private fun onSuccessAuth(user: FirebaseUser?) {
        Toast.makeText(context, user?.email, Toast.LENGTH_SHORT).show()
        prefs.setUserStateLogIn()
        router.goToCameraActivity(/*user?.getIdToken(true)?.result?.token ?:*/ EMPTY_STRING) //TODO: Token?
    }

}
