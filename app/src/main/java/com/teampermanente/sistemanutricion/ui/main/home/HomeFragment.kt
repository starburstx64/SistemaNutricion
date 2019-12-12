package com.teampermanente.sistemanutricion.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.teampermanente.sistemanutricion.R
import org.json.JSONArray

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var imcLineChart : LineChart
    private lateinit var performanceSpinner : Spinner
    private lateinit var sessionSpinner : Spinner

    private lateinit var imcTextView: TextView
    private lateinit var sessionIMC: TextView
    private lateinit var sessionWeight: TextView
    private lateinit var sessionPercent: TextView
    private lateinit var sessionWaist: TextView
    private lateinit var sessionHip: TextView
    private lateinit var sessionBrachial: TextView

    private var selectedSession = 1

    private val model by lazy { ViewModelProviders.of(activity!!).get(HomeViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
/*        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })*/

        imcTextView = root.findViewById(R.id.home_textview_imc) as TextView
        sessionIMC = root.findViewById(R.id.home_textview_sessionIMC) as TextView
        sessionWeight = root.findViewById(R.id.home_textview_sessionWeight) as TextView
        sessionPercent = root.findViewById(R.id.home_textview_sessionPercent) as TextView
        sessionWaist = root.findViewById(R.id.home_textview_sessionWaist) as TextView
        sessionHip = root.findViewById(R.id.home_textview_sessionHip) as TextView
        sessionBrachial = root.findViewById(R.id.home_textview_sessionBrachial) as TextView

        val performanceArrayList = listOf("Peso")
        val performanceAdapter = ArrayAdapter<String>(root.context, android.R.layout.simple_spinner_item, performanceArrayList)
        performanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        performanceSpinner = root.findViewById(R.id.home_spinner_performance) as Spinner
        performanceSpinner.adapter = performanceAdapter

        val entries = listOf(Entry(0f, 5f), Entry(10f, 15f))
        val entries2 = listOf(Entry(5f, 15f), Entry(20f, 25f))

        imcLineChart = root.findViewById(R.id.home_lineChart_imc) as LineChart
        imcLineChart.data = LineData(LineDataSet(entries, "Peso"), LineDataSet(entries2, "Sesiones"))

        updateStatistics()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Test", model.idUsuario)

        val queue = Volley.newRequestQueue(view.context)
        val url = "https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/numerosesiones?expedienteId=${model.idUsuario}"
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                val numSessions = JSONArray(response).getJSONObject(0).getString("numeroDeSeguimientos").toInt()
                val sessionsList = mutableListOf<String>()

                for (i in 0 until numSessions) {
                    sessionsList.add("Sesion ${i + 1}")
                }

                val sessionAdapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item, sessionsList)
                sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sessionSpinner = view.findViewById(R.id.home_spinner_session) as Spinner
                sessionSpinner.adapter = sessionAdapter
                sessionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        selectedSession = p2 + 1
                        updateStatistics()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }

                setQurrentIMC(numSessions)
            },
            Response.ErrorListener { Log.d("Node", "Error rasa") }
        )

        queue.add(stringRequest)
    }

    fun updateStatistics() {
        val queue = Volley.newRequestQueue(context)
        val url = String.format("https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerseguimiento?expedienteId=${model.idUsuario}&idSeguimiento=$selectedSession")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                val jsonArray = JSONArray(response)
                val jsonObject = jsonArray.getJSONObject(0)

                val imc = jsonObject.getString("imc")
                val peso = jsonObject.getString("peso")
                val grasa = jsonObject.getString("porGrasa")
                val cintura = jsonObject.getString("cCintura")
                val cadera = jsonObject.getString("cCadera")
                val braquial = jsonObject.getString("cBraquial")

                //imcTextView.text = jsonObject.getString("imc")
                sessionIMC.text = "IMC: $imc"
                sessionWeight.text = "Peso: $peso kg"
                sessionPercent.text = "Porcentaje de grasa: $grasa %"
                sessionWaist.text = "Circunferencia de cintura: $cintura cm"
                sessionHip.text = "Circunferencia de cadera: $cadera cm"
                sessionBrachial.text = "Circunferencia braquial: $braquial cm"
            },
            Response.ErrorListener { Log.d("Node", "Error rasa") }
        )
        queue.add(stringRequest)
    }

    fun setQurrentIMC(idSession : Int) {
        val queue = Volley.newRequestQueue(context)
        val url = String.format("https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerseguimiento?expedienteId=${model.idUsuario}&idSeguimiento=$idSession")

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                val jsonArray = JSONArray(response)
                val jsonObject = jsonArray.getJSONObject(0)

                imcTextView.text = jsonObject.getString("imc")
            },
            Response.ErrorListener { Log.d("Node", "Error rasa") }
        )
        queue.add(stringRequest)
    }
}