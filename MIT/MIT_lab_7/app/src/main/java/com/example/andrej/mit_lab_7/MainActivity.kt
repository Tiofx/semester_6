package com.example.andrej.mit_lab_7

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.PopupWindow
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val buttonTask1 by lazy {
        findViewById(R.id.buttonTask1) as Button
    }

    val buttonPdf by lazy {
        findViewById(R.id.buttonPdf) as Button
    }

    val buttonClearPreferences by lazy {
        findViewById(R.id.buttonClearPreferences) as Button
    }

    fun permitAll() = StrictMode.ThreadPolicy.Builder()
            .permitAll()
            .build()
            .let { StrictMode.setThreadPolicy(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: delete !!!
        permitAll()

        taskFour()

        buttonTask1.setOnClickListener {
            taskOne()
        }

        buttonPdf.setOnClickListener {
            Intent(this@MainActivity, PdfFileInterface::class.java)
                    .let { startActivity(it) }
        }

        buttonClearPreferences.setOnClickListener {
            Util.clearPreference(this@MainActivity)
        }
    }

    fun taskOne() {
        ((getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo
                ?.takeIf { it.isConnected }
                ?.detailedState?.toString()
                ?: "Ошибка подключения к сети :("
                )
                .let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                            .show()
                }
    }

    fun taskFour() {
        if (!Util.getPreference(this, "doNotShow")) {
            (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    .inflate(R.layout.popup, null)
                    .let {
                        PopupWindow(it,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                true
                        ).apply {
                            (it.findViewById(R.id.buttonOk) as Button)
                                    .setOnClickListener { _ ->
                                        this.dismiss()
                                        Util.setPreference(this@MainActivity, "doNotShow",
                                                (it.findViewById(R.id.checkBoxDoNotShowAgain) as CheckBox)
                                                        .isChecked)
                                    }
                            buttonTask1.post { this.showAsDropDown(this@MainActivity.findViewById(R.id.root), 0, 0) }
                        }
                    }
        }
    }
}

