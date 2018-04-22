package com.haretskiy.pavel.magiccamera.models

import com.google.firebase.auth.FirebaseUser

data class FirebaseLoginResponse(
        var user: FirebaseUser? = null,
        var errorMessage: String = ""
)