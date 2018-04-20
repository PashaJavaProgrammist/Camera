package com.haretskiy.pavel.magiccamera.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.haretskiy.pavel.magiccamera.BUNDLE_KEY_SIGN
import com.haretskiy.pavel.magiccamera.R
import com.haretskiy.pavel.magiccamera.SIGN_IN_FLAG
import com.haretskiy.pavel.magiccamera.SIGN_UP
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.android.synthetic.main.fragment_sign.view.*
import org.koin.android.ext.android.inject


class SignFragment : Fragment(), View.OnClickListener {

    private val mAuth: FirebaseAuth by inject()

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
        if (isSignIn) {
            signIn(emailStr, passwordStr)
        } else {
            val repeatePasswordStr = repeate_password.text.toString()
            if (passwordStr.equals(repeatePasswordStr)) {
                signUp(emailStr, passwordStr)
            } else {
                Toast.makeText(context, "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        Toast.makeText(context, "Success: +${user?.email}", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Fail: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
    }


    private fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        Toast.makeText(context, "Success: +${user?.email}", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Fail: ${task.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}
