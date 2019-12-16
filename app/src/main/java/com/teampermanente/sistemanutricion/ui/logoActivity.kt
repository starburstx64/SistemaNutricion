package com.teampermanente.sistemanutricion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.teampermanente.sistemanutricion.R
import com.teampermanente.sistemanutricion.ui.login.LoginActivity
import com.teampermanente.sistemanutricion.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class logoActivity : AppCompatActivity() {
    private lateinit var logo:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)


        logo = findViewById(R.id.logo)
        val animacion: Animation
        val animacionDos: Animation

        animacion = AnimationUtils.loadAnimation(this, R.anim.animacion)
        animacionDos = AnimationUtils.loadAnimation(this, R.anim.animacion_button)

        logo.animation = animacion

        val background = object : Thread() {
            override fun run() {
                super.run()

                try {
                    sleep((3 * 1000).toLong())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val toMainIntent = Intent(baseContext, LoginActivity::class.java)
                startActivity(toMainIntent)
                finish()
            }
        }
        background.start()
    }

}
