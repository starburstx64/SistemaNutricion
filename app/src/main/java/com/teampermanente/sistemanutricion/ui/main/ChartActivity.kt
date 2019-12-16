package com.teampermanente.sistemanutricion.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.LineChart
import com.teampermanente.sistemanutricion.R

class ChartActivity : AppCompatActivity() {

    private lateinit var lineChart : LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        lineChart = findViewById(R.id.home_lineChart_imc)

    }
}
