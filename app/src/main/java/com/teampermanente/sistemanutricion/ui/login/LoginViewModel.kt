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

    fun handleLoginResponse(response: String) {
        if (response != "[]") {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = "David Segovia"))
        }

        else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun login(fileKey: String, context: Context) : StringRequest {
        // can be launched in a separate asynchronous job
        val queue = Volley.newRequestQueue(context)
        val url = String.format("https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerexpediente?expedienteId=$fileKey")

        // Request a string response from the provided URL.
        return StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                if (response != "[]") {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = "David Segovia"))
                }

                else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            },
            Response.ErrorListener { Log.d("Node", "Error rasa") }
        )

        // Add the request to the RequestQueue.
        //queue.add(stringRequest)

        /*loginRepository.login(fileKey, context, object : LoginCallback {
            override fun onCallback(loginResult: Result<LoggedInUser>) {
                if (loginResult is Result.Success) {
                    _loginResult.value =
                        LoginResult(success = LoggedInUserView(displayName = loginResult.data.displayName))
                }

                else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
        })*/
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
