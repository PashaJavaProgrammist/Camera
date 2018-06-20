package com.haretskiy.pavel.magiccamera.ui.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haretskiy.pavel.magiccamera.*
import com.haretskiy.pavel.magiccamera.models.FirebaseLoginResponse
import com.haretskiy.pavel.magiccamera.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.android.synthetic.main.fragment_sign.view.*
import org.koin.android.architecture.ext.viewModel
import java.util.regex.Pattern

class LoginFragment : Fragment(), View.OnClickListener {

    private val loginViewModel: LoginViewModel by viewModel()

    //this flag shows SignIn or SignUp screen
    private var isSignInScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isSignInScreen = arguments?.get(BUNDLE_KEY_SIGN)?.equals(SIGN_IN_FLAG) ?: false

        loginViewModel.userInfo.observe(this, Observer<FirebaseLoginResponse> {
            login_progress.visibility = View.GONE
            when (it?.user) {
                null -> loginViewModel.onErrorAuth(it?.errorMessage ?: EMPTY_STRING)
                else -> loginViewModel.onSuccessAuth(it.user?.email ?: EMPTY_STRING)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign, container, false)
        if (isSignInScreen) {
            view.password_repeat_input.visibility = View.GONE
            view.email_sign_in_button.text = SIGN_IN
        } else {
            view.password_repeat_input.visibility = View.VISIBLE
            view.email_sign_in_button.text = SIGN_UP
        }
        view.email_sign_in_button.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manageInputLayouts()
    }

    override fun onClick(v: View?) {
        when (v) {
            email_sign_in_button -> sign()
        }
    }

    private fun sign() {
        login_progress.visibility = View.VISIBLE
        loginViewModel.sign(email.text.toString(), password.text.toString(), if (!isSignInScreen) repeat_password.text.toString() else EMPTY_STRING)
    }

    private fun manageInputLayouts() {
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int): Unit = showEmailErrorText()
        })

        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int): Unit = showPasswordErrorText()
        })

        repeat_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int): Unit = showRepeatPasswordErrorText()
        })
    }

    private fun showEmailErrorText() {

        val emailText = email.text.toString()
        val patternEmail = Pattern.compile(PATTERN_EMAIL2)
        val matcher = patternEmail.matcher(emailText)
        email_input.setErrorTextAppearance(R.style.ErrorTextRed)
        if (emailText.isEmpty()) {
            email_input.error = getString(R.string.email_cant_empty)
        } else if (!matcher.find()) {
            email_input.error = getString(R.string.wrong_email_format)
        } else if (!emailText.isEmpty()) {
            email_input.setErrorTextAppearance(R.style.ErrorTextGreen)
            email_input.error = getString(R.string.succes_email)
        } else {
            email_input.error = EMPTY_STRING
        }
    }

    private fun showPasswordErrorText() {
        val passwordText = password.text.toString()
        if (passwordText.length < 6) {
            password_input.setErrorTextAppearance(R.style.ErrorTextRed)
            password_input.error = getString(R.string.pass_more_six)
        } else {
            password_input.setErrorTextAppearance(R.style.ErrorTextGreen)
            password_input.error = getString(R.string.pass_fits)
        }
    }

    private fun showRepeatPasswordErrorText() {
        val passwordText = password.text.toString()
        val passwordRepeatText = repeat_password.text.toString()
        if (passwordRepeatText != passwordText) {
            password_repeat_input.setErrorTextAppearance(R.style.ErrorTextRed)
            password_repeat_input.error = getString(R.string.pass_d_match)
        } else {
            password_repeat_input.setErrorTextAppearance(R.style.ErrorTextGreen)
            password_repeat_input.error = getString(R.string.pass_match)
        }
    }

}
