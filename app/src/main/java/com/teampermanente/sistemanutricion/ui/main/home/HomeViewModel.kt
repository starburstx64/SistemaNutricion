package com.teampermanente.sistemanutricion.ui.main.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class HomeViewModel : ViewModel() {

    data class Session(
        val id : Int,
        val numSession : Int,
        val peso : Double,
        val imc : Double,
        val circCintura : Double,
        val circCadera : Double,
        val circBraquial : Double,
        val circMuneca : Double,
        val porcGrasa : Double,
        val porcAgua : Double,
        val masaMagra : Double,
        val masaGrasa : Double,
        val fechaSesion : String
    )

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _sessionsList = MutableLiveData<List<Session>>()
    val sessionsList : LiveData<List<Session>> = _sessionsList

    var idUsuario : String = "dad12"
    var username : String = ""
    var userLastName : String = ""
    var userMail : String = ""

    fun getSessionsData(context : Context) {

        val queue = Volley.newRequestQueue(context)
        val url = "https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerultimoseguimiento?expedienteId=${idUsuario}"

        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> {
            response ->

            if (response != "[]") {

                val sessionsArray = JSONArray(response)
                val sessions = mutableListOf<Session>()

                for (i in 0 until sessionsArray.length()) {
                    val jsonObject = sessionsArray.getJSONObject(i)

                    val session = Session(
                        id = jsonObject.getInt("idSeguimiento"),
                        numSession = jsonObject.getInt("idSeguimiento"),
                        peso = jsonObject.getDouble("peso"),
                        imc = jsonObject.getDouble("imc"),
                        circCintura = jsonObject.getDouble("cCintura"),
                        circCadera = jsonObject.getDouble("cCadera"),
                        circBraquial = jsonObject.getDouble("cBraquial"),
                        circMuneca = jsonObject.getDouble("cMuneca"),
                        porcGrasa = jsonObject.getDouble("porGrasa"),
                        porcAgua = jsonObject.getDouble("porAgua"),
                        masaMagra = jsonObject.getDouble("masaMagra"),
                        masaGrasa = jsonObject.getDouble("masaGrasa"),
                        fechaSesion = jsonObject.getString("fechaSesion")
                    )

                    sessions.add(session)
                }

                _sessionsList.value = sessions.toList()
            }

            else {
                _sessionsList.value = listOf()
            }

        }, Response.ErrorListener {
            Log.e("ERROR", "${it.message}")
        })

        queue.add(stringRequest)
    }
}