package com.teampermanente.sistemanutricion.ui.main.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.teampermanente.sistemanutricion.R
import com.teampermanente.sistemanutricion.ui.main.ChartActivity
import java.lang.Exception

class HomeFragment : Fragment() {

    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var scrollView: ScrollView

    private lateinit var imcLineChart : LineChart
    private lateinit var performanceSpinner : Spinner
    private lateinit var sessionSpinner : Spinner
    private lateinit var userNameTextView: TextView
    private lateinit var imcTextView: TextView
    private lateinit var sessionIMC: TextView
    private lateinit var sessionWeight: TextView
    private lateinit var sessionPercent: TextView
    private lateinit var sessionWaist: TextView
    private lateinit var sessionHip: TextView
    private lateinit var sessionBrachial: TextView
    private lateinit var sessionPercentWater: TextView
    private lateinit var sessionMuneca: TextView
    private lateinit var sessionMasaMagra: TextView
    private lateinit var sessionMasaGrasa: TextView
    private lateinit var sessionDate: TextView
    private lateinit var reloadButton: Button

    private val entriesForIntent = mutableListOf<Pair<Float, Float>>()
    private var sessionQuartersForIntent = mutableListOf<String>()
    private var entryNameForIntent = ""

    private val model by lazy { ViewModelProviders.of(activity!!).get(HomeViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        scrollView = root.findViewById(R.id.scrollView2) as ScrollView
        loadingProgressBar = root.findViewById(R.id.loading) as ProgressBar

        userNameTextView = root.findViewById(R.id.home_textview_username) as TextView
        userNameTextView.text = model.username

        imcTextView = root.findViewById(R.id.home_textview_imc) as TextView
        sessionIMC = root.findViewById(R.id.home_textview_sessionIMC) as TextView
        sessionWeight = root.findViewById(R.id.home_textview_sessionWeight) as TextView
        sessionPercent = root.findViewById(R.id.home_textview_sessionPercent) as TextView
        sessionWaist = root.findViewById(R.id.home_textview_sessionWaist) as TextView
        sessionHip = root.findViewById(R.id.home_textview_sessionHip) as TextView
        sessionBrachial = root.findViewById(R.id.home_textview_sessionBrachial) as TextView
        sessionPercentWater = root.findViewById(R.id.home_textview_sessionPercentWater) as TextView
        sessionMuneca = root.findViewById(R.id.home_textview_sessionMuneca) as TextView
        sessionMasaMagra = root.findViewById(R.id.home_textview_sessionMasaMagra) as TextView
        sessionMasaGrasa = root.findViewById(R.id.home_textview_sessionMasaGrasa) as TextView
        sessionDate = root.findViewById(R.id.home_textview_sessionDate) as TextView
        reloadButton = root.findViewById(R.id.home_button_reload) as Button

        reloadButton.setOnClickListener {
            model.getSessionsData(root.context)
        }

        val performanceArrayList = listOf("Peso", "IMC", "Circ. Cintura", "Circ. Cadera", "Circ. Braquial",
            "Circ. Muñeca", "% Grasa", "% Agua", "Masa Magra", "Masa Grasa")

        val performanceAdapter = ArrayAdapter<String>(root.context, android.R.layout.simple_spinner_item, performanceArrayList)
        performanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        performanceSpinner = root.findViewById(R.id.home_spinner_performance) as Spinner
        performanceSpinner.adapter = performanceAdapter
        performanceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateChart(p2, performanceArrayList[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }

        imcLineChart = root.findViewById(R.id.home_lineChart_imc) as LineChart
        imcLineChart.setOnClickListener {

            if (imcLineChart.data == null) {
                return@setOnClickListener
            }

            val toLineChartIntent = Intent(root.context, ChartActivity::class.java)
            toLineChartIntent.putExtra("entries", entriesForIntent.toTypedArray())
            toLineChartIntent.putExtra("sessionQuartets", sessionQuartersForIntent.toTypedArray())
            toLineChartIntent.putExtra("entryName", entryNameForIntent)
            startActivity(toLineChartIntent)
        }

        model.getSessionsData(root.context)
        model.sessionsList.observe(activity as LifecycleOwner, Observer {
            val sessions = it ?: return@Observer

            val homeRoot = root.findViewById(R.id.home_root) as ConstraintLayout
            homeRoot.setBackgroundResource(R.color.secondaryTextColor)

            loadingProgressBar.visibility = View.GONE

            if (sessions.isNotEmpty()) {

                val twoDigits = "%.2f"
                imcTextView.text = twoDigits.format(sessions[0].imc)

                updateStatistics(sessions[0])

                val sessionsList = mutableListOf<String>()
                for (i in sessions.indices.reversed()) {
                    sessionsList.add("Sesion ${i + 1}")
                }

                val sessionAdapter = ArrayAdapter<String>(root.context, android.R.layout.simple_spinner_item, sessionsList)
                sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sessionSpinner = root.findViewById(R.id.home_spinner_session) as Spinner
                sessionSpinner.adapter = sessionAdapter
                sessionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        updateStatistics(sessions[p2])
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }

                loadingProgressBar.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                updateChart(0, "Peso")
            }

            else {
                Log.d("Test", "Lista vacia")
                val noSessionsText = root.findViewById(R.id.home_textview_noSessions) as TextView
                noSessionsText.visibility = View.VISIBLE
                val imgSessions= root.findViewById(R.id.imgEmpty)as ImageView
                imgSessions.visibility = View.VISIBLE

                reloadButton.visibility = View.VISIBLE
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        model.sessionsList.removeObservers(activity as LifecycleOwner)
    }

    private fun updateStatistics(session : HomeViewModel.Session) {

        val twoDigits = "%.2f"
        sessionIMC.text = getString(R.string.session_imc, twoDigits.format(session.imc))
        sessionWeight.text = getString(R.string.session_peso, twoDigits.format(session.peso))
        sessionPercent.text = getString(R.string.session_porGrasa, twoDigits.format(session.porcGrasa))
        sessionWaist.text = getString(R.string.session_circCintura, twoDigits.format(session.circCintura))
        sessionHip.text = getString(R.string.session_circCadera, twoDigits.format(session.circCadera))
        sessionBrachial.text = getString(R.string.session_circBraquial, twoDigits.format(session.circBraquial))
        sessionMuneca.text = getString(R.string.session_circMuneca, twoDigits.format(session.circMuneca))
        sessionPercentWater.text = getString(R.string.session_porWater, twoDigits.format(session.porcAgua))
        sessionMasaMagra.text = getString(R.string.session_masaMagra, twoDigits.format(session.masaMagra))
        sessionMasaGrasa.text = getString(R.string.session_masaGrasa, twoDigits.format(session.masaGrasa))

        val simpleDate = session.fechaSesion.split("T")[0]
        val dateParts = simpleDate.split("-")

        val year = dateParts[0]
        val month = dateParts[1]
        val day = dateParts[2]

        val months = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
                            "Septiembre", "Octubre", "Noviembre", "Diciembre")

        sessionDate.text = "$day de ${months[month.toInt() - 1]}, $year"
    }

    private fun updateChart(selectedIndex : Int, entryName : String) {

        if (model.sessionsList.value.isNullOrEmpty()) {
            return
        }

        entriesForIntent.clear()
        sessionQuartersForIntent.clear()
        entryNameForIntent = entryName

        val entries = mutableListOf<Entry>()

        var currentSession = 1
        val sessionQuarters = mutableListOf<String>()
        for (session in model.sessionsList.value!!.reversed()) {

            val sessionData = when (selectedIndex) {
                0 -> session.peso
                1 -> session.imc
                2 -> session.circCintura
                3 -> session.circCadera
                4 -> session.circBraquial
                5 -> session.circMuneca
                6 -> session.porcGrasa
                7 -> session.porcAgua
                8 -> session.masaMagra
                9 -> session.masaGrasa

                else -> session.peso
            }

            entriesForIntent.add(Pair(currentSession.toFloat() - 1, sessionData.toFloat()))

            entries.add(Entry(currentSession.toFloat() - 1, sessionData.toFloat()))
            sessionQuarters.add("Sesion $currentSession")
            currentSession++
        }

        sessionQuartersForIntent = sessionQuarters

        val lineDataSet = LineDataSet(entries, entryName)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.valueTextSize = 10f

        val valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {

                return if (value < 0 || value >= sessionQuarters.size) "" else {
                    sessionQuarters[value.toInt()]
                }
            }
        }

        val xAxis = imcLineChart.xAxis
        xAxis.granularity = 1f
        xAxis.valueFormatter = valueFormatter

        imcLineChart.data = LineData(lineDataSet)
        imcLineChart.invalidate()
    }
}