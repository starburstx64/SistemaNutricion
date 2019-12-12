package com.teampermanente.sistemanutricion.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.teampermanente.sistemanutricion.data.model.LoggedInUser
import com.teampermanente.sistemanutricion.ui.login.LoginViewModel
import java.lang.Exception

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(fileKey: String, context : Context, callback1 : LoginViewModel.LoginCallback, callback2: LoginViewModel.LoginCallback) {
        try {
            // TODO: handle loggedInUser authentication

            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(context)
            val url = String.format("https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerexpediente?expedienteId=$fileKey")

            // Request a string response from the provided URL.
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->

                    if (response != "[]") {
                        callback1.onCallback(Result.Success(LoggedInUser("1", "David Segovia")))
                        callback2.onCallback(Result.Success(LoggedInUser("1", "David Segovia")))
                    }

                    else {
                        callback1.onCallback(Result.Error(Exception()))
                        callback2.onCallback(Result.Error(Exception()))
                    }
                },
                Response.ErrorListener { Log.d("Node", "Error rasa") }
            )

            // Add the request to the RequestQueue.
            queue.add(stringRequest)

        } catch (e: Throwable) {

        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

