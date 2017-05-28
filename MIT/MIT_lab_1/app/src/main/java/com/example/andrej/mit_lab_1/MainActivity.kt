package com.example.andrej.mit_lab_1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById(R.id.buttonPressMe) as Button
        val text = findViewById(R.id.txtName) as TextView

        btn.setOnClickListener { text.text = "Поздравляю!!! Ты нажал на кнопку." }
    }
}
