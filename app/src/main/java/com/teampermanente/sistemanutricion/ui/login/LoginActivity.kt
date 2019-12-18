package com.teampermanente.sistemanutricion.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory
import com.amazonaws.regions.Regions
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.google.gson.JsonParser

import com.teampermanente.sistemanutricion.R
import com.teampermanente.sistemanutricion.clientsdk.NutriologoQuerysClient
import com.teampermanente.sistemanutricion.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.clave)
        val loginButton = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        val animacion: Animation
        val animacionDos: Animation


        animacion = AnimationUtils.loadAnimation(this, R.anim.animacion_right)
        animacionDos = AnimationUtils.loadAnimation(this, R.anim.animacion_button)

        //imageView2.animation = animacion
        textInputLayout_emailLogin1.animation = animacion
        login.animation = animacionDos
        val background = object : Thread() {
            override fun run() {
                super.run()

                try {
                    sleep((3 * 1000).toLong())

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)

            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)

                //Complete and destroy login activity once successful
                val toMainIntent = Intent(this, MainActivity::class.java)
                toMainIntent.putExtra("idUsuario", username.text.toString())
                toMainIntent.putExtra("username", loginResult.success.displayName)
                toMainIntent.putExtra("userLastName", loginResult.success.lastName)
                toMainIntent.putExtra("userMail", loginResult.success.userMail)
                startActivity(toMainIntent)

                finish()
            }
            setResult(Activity.RESULT_OK)
        })

        username.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> loginViewModel.login(
                        username.text.toString(),
                        this@LoginActivity
                    )
                }

                false
            }

            loginButton.setOnClickListener {
                view = it
                loading.visibility = View.VISIBLE
                if (username.text.isNotEmpty()) {

                    loginViewModel.login(username.text.toString(), this@LoginActivity)

                } else {
                    Snackbar.make(it, "Campo Vacio,LLenelo.", Snackbar.LENGTH_LONG).show()

                }


            }

        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
         Toast.makeText(
             applicationContext,
             "$welcome $displayName",
             Toast.LENGTH_LONG
         ).show()

    }

    private fun showLoginFailed(@StringRes errorString: Int) {
       // Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
        Snackbar.make(view,"Clave Incorrecta!! :'(",Snackbar.LENGTH_LONG).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
