package com.teampermanente.sistemanutricion.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.teampermanente.sistemanutricion.data.LoginRepository
import com.teampermanente.sistemanutricion.data.Result

import com.teampermanente.sistemanutricion.R
import com.teampermanente.sistemanutricion.data.model.LoggedInUser
import java.lang.Exception

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    interface LoginCallback {
        fun onCallback(loginResult : Result<LoggedInUser>)
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(fileKey: String, context: Context) {
        // can be launched in a separate asynchronous job
        loginRepository.login(fileKey, context, object : LoginCallback {
            override fun onCallback(loginResult: Result<LoggedInUser>) {
                if (loginResult is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = loginResult.data.displayName,
                            lastName = loginResult.data.lastName, userMail = loginResult.data.userMail))
                }

                else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        })
    }

    fun loginDataChanged(username: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {

        return username.isNotBlank()

        /*return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }*/
    }
}
