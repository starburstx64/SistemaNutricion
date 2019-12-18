package com.teampermanente.sistemanutricion.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.teampermanente.sistemanutricion.R

class ChartActivity : AppCompatActivity() {

    private lateinit var lineChart : LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        lineChart = findViewById(R.id.home_lineChart_imc)

        @Suppress("UNCHECKED_CAST")
        val entries = intent.getSerializableExtra("entries") as Array<Pair<Float, Float>>
        val quartets = intent.getStringArrayExtra("sessionQuartets")!!
        val entryName = intent.getStringExtra("entryName")

        val lineEntries = mutableListOf<Entry>()

        for (i in entries) {
            lineEntries.add(Entry(i.first, i.second))
        }

        val lineDataSet = LineDataSet(lineEntries, entryName)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.valueTextSize = 10f

        val valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return if (value < 0 || value >= quartets.size) "" else {
                    quartets[value.toInt()]
                }
            }
        }

        val xAxis = lineChart.xAxis
        xAxis.granularity = 1f
        xAxis.valueFormatter = valueFormatter

        lineChart.data = LineData(lineDataSet)
        lineChart.invalidate()
    }
}
