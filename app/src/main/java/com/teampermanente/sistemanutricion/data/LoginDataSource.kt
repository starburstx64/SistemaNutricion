package com.teampermanente.sistemanutricion.data

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.teampermanente.sistemanutricion.data.model.LoggedInUser
import com.teampermanente.sistemanutricion.ui.login.LoginViewModel
import org.json.JSONArray
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

                        val jsonObject = JSONArray(response).getJSONObject(0)
                        val lastName = jsonObject.getString("apellidos")
                        val mail = jsonObject.getString("correo")

                        callback1.onCallback(Result.Success(LoggedInUser(jsonObject.getString("clave"), jsonObject.getString("nombre"), lastName, mail)))
                        callback2.onCallback(Result.Success(LoggedInUser(jsonObject.getString("clave"), jsonObject.getString("nombre"), lastName, mail)))
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

