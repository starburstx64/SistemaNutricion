package com.teampermanente.sistemanutricion.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val displayName: String,
    val lastName : String,
    val userMail : String
    //... other data fields that may be accessible to the UI
)
