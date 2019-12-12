package com.teampermanente.sistemanutricion.data

import android.content.Context
import com.teampermanente.sistemanutricion.data.model.LoggedInUser
import com.teampermanente.sistemanutricion.ui.login.LoginViewModel

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(fileKey: String, context: Context, callback : LoginViewModel.LoginCallback) {
        // handle login
        dataSource.login(fileKey, context, callback, object : LoginViewModel.LoginCallback {
            override fun onCallback(loginResult: Result<LoggedInUser>) {
                if (loginResult is Result.Success) {
                    setLoggedInUser(loginResult.data)
                }
            }
        })
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
