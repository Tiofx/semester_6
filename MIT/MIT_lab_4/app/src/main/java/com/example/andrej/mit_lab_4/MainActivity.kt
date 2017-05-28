package com.example.andrej.mit_lab_4

import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.andrej.mit_lab_2.Operation

class MainActivity : AppCompatActivity() {

    val gestureLibrary: GestureLibrary by lazy {
        GestureLibraries
                .fromRawResource(this@MainActivity, R.raw.gestures)
                .apply { if (!load()) finish() }
    }

    val gestureView: GestureOverlayView by lazy {
        this@MainActivity.findViewById(R.id.gestureView) as GestureOverlayView
    }

    val textView: TextView by lazy {
        this@MainActivity.findViewById(R.id.textView) as TextView
    }

    val operationView by lazy {
        this@MainActivity.findViewById(R.id.txtViewOperation) as TextView
    }

    val previousOperandView by lazy {
        this@MainActivity.findViewById(R.id.txtViewOperand1) as TextView
    }

    val currentOperandView by lazy {
        this@MainActivity.findViewById(R.id.txtViewOperand2) as TextView
    }

    val calculator: Calculator by lazy {
        Calculator(currentOperandView, previousOperandView, operationView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestureView.addOnGesturePerformedListener { _, gesture ->
            val predictions = gestureLibrary.recognize(gesture)

            if (predictions.isNotEmpty()) {
                val prediction = predictions[0]

                if (prediction.score > 1.0) {
                    textView.text = prediction.name

                    when (prediction.name) {
                        "+" -> calculator.press(Operation.PLUS)
                        "-" -> calculator.press(Operation.MINUS)
                        "=" -> calculator.press(Operation.EQUALLY)
                        "reset" -> calculator.press(Operation.AC)
                        "deleteLast" -> Unit
                        else -> if (prediction.name.isInt(16)) calculator.pressNumber(prediction.name)
                    }
                } else {
                    textView.text = "Жест неизвестен"
                }
            }
        }
    }
}

class Calculator(currentOperand: TextView,
                 val previousOperandView: TextView,
                 val operationView: TextView,
                 numberSystem: Int = 16) :
        com.example.andrej.mit_lab_2.Calculator(currentOperand, numberSystem) {

    override protected var previousOperand: String? = null
        set(value) {
            previousOperandView.text = value.toString()
            field = value
        }

    override protected var currentOperation = Operation.AC
        set(value) {
            operationView.text = value.toString()
            field = value
        }

    override fun calculate(operation: Operation) =
            when (operation) {
                Operation.PLUS -> plusCalculate(previousOperand!!, currentValue)
                Operation.MINUS -> minusCalculate(previousOperand!!, currentValue)
                else -> super.calculate(operation)
            }

    private fun minusCalculate(a: String, b: String) =
            (a.toInt(numberSystem)
                    - b.toInt(numberSystem))
                    .toString(numberSystem)
                    .toUpperCase()

    private fun plusCalculate(a: String, b: String) =
            (a.toInt(numberSystem)
                    + b.toInt(numberSystem))
                    .toString(numberSystem)
                    .toUpperCase()
}


infix fun String.isInt(radix: Int) = toIntOrNull(radix) != null