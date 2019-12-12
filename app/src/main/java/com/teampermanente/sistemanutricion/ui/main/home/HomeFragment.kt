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

    private var selectedSession = 1

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

        imcTextView = root.findViewById(R.id.home_textview_imc)

        val performanceArrayList = listOf("Peso")
        val performanceAdapter = ArrayAdapter<String>(root.context, android.R.layout.simple_spinner_item, performanceArrayList)
        performanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        performanceSpinner = root.findViewById(R.id.home_spinner_performance) as Spinner
        performanceSpinner.adapter = performanceAdapter

        val sessionArrayList = listOf("Sesion 3")
        val sessionAdapter = ArrayAdapter<String>(root.context, android.R.layout.simple_spinner_item, sessionArrayList)
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sessionSpinner = root.findViewById(R.id.home_spinner_session) as Spinner
        sessionSpinner.adapter = sessionAdapter
        sessionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSession = p2
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        val entries = listOf(Entry(0f, 5f), Entry(10f, 15f))
        val entries2 = listOf(Entry(5f, 15f), Entry(20f, 25f))

        imcLineChart = root.findViewById(R.id.home_lineChart_imc) as LineChart
        imcLineChart.data = LineData(LineDataSet(entries, "Peso"), LineDataSet(entries2, "Sesiones"))

        val queue = Volley.newRequestQueue(root.context)
        val url = String.format("https://qpnwxks3e9.execute-api.us-east-1.amazonaws.com/Dev/obtenerseguimiento?expedienteId=dad12&idSeguimiento=$selectedSession")

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

        return root
    }
}