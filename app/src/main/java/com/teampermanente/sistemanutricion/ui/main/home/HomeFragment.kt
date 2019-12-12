package com.teampermanente.sistemanutricion.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.teampermanente.sistemanutricion.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var imcLineChart : LineChart
    private lateinit var performanceSpinner : Spinner
    private lateinit var sessionSpinner : Spinner

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

        val entries = listOf(Entry(0f, 5f), Entry(10f, 15f))
        val entries2 = listOf(Entry(5f, 15f), Entry(20f, 25f))

        imcLineChart = root.findViewById(R.id.home_lineChart_imc) as LineChart
        imcLineChart.data = LineData(LineDataSet(entries, "Peso"), LineDataSet(entries2, "Sesiones"))

        return root
    }
}