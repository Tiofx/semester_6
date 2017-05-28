package com.example.andrej.mit_lab_2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TableLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val number10 = findViewById(R.id.numbersFor10) as TableLayout
        val number16 = findViewById(R.id.numbersFor16) as TableLayout

        val textView = findViewById(R.id.textView) as TextView
        val buttonMod = findViewById(R.id.buttonMod) as Button
        val buttonAc = findViewById(R.id.buttonAc) as Button
        val buttonEqually = findViewById(R.id.buttonEquall) as Button
        val numberNotation = findViewById(R.id.numberNotation) as Switch

        val calculator = Calculator(textView)



        buttonMod.setOnClickListener { calculator.press(Operation.MOD) }
        buttonAc.setOnClickListener { calculator.press(Operation.AC) }
        buttonEqually.setOnClickListener { calculator.press(Operation.EQUALLY) }

        numberNotation.setOnCheckedChangeListener { _, isChecked ->
            calculator.reset()
            calculator.numberSystem = if (isChecked) 16 else 10

            if (isChecked) {
                number10.visibility = View.INVISIBLE
                number16.visibility = View.VISIBLE
            } else {
                number10.visibility = View.VISIBLE
                number16.visibility = View.INVISIBLE
            }
        }

        number10.getAllChildren()
                .filter { it is Button }
                .map { it as Button }
                .forEach { button ->
                    button.setOnClickListener {
                        calculator.pressNumber(button.text.toString())
                    }
                }

        number16.getAllChildren()
                .filter { it is Button }
                .map { it as Button }
                .forEach { button ->
                    button.setOnClickListener {
                        calculator.pressNumber(button.text.toString())
                    }
                }
    }
}

fun ViewGroup.getAllChildren(): List<View> = (0..childCount - 1)
        .map { getChildAt(it) }
        .partition { it !is ViewGroup }
        .let {
            it.first + (it.second
                    .flatMap { viewGroup -> (viewGroup as ViewGroup).getAllChildren() })
        }


open class Calculator(val textView: TextView, var numberSystem: Int = 10) {
    var currentValue = "0"
        set(value) {
            field = value
            textView.text = field
        }

    protected var previousOperand: String? = null
    protected var currentOperation = Operation.AC
    protected var isNewOperand = false

    fun pressNumber(number: String) {
        if (currentValue.length >= 6) return

        currentValue = if (currentValue == "0" || isNewOperand) {
            isNewOperand = false
            number
        } else currentValue + number

    }

    fun press(operation: Operation) {
        when (operation) {
            Operation.AC -> reset()
            Operation.EQUALLY -> {
                currentValue = calculate(currentOperation)
                currentOperation = Operation.AC
                previousOperand = null
            }
            else -> {
                isNewOperand = true
                currentOperation = operation
                if (previousOperand == null) {
                    previousOperand = currentValue
                    return
                }

                currentValue = modCalculate(previousOperand!!, currentValue)
                previousOperand = currentValue
            }
        }
    }

    protected fun modCalculate(a: String, b: String) =
            (a.toInt(numberSystem)
                    % b.toInt(numberSystem))
                    .toString(numberSystem)
                    .toUpperCase()

    protected fun calculate(operation: Operation) =
            when (operation) {
                Operation.MOD -> modCalculate(previousOperand!!, currentValue)
                else -> currentValue
            }


    fun reset() {
        currentOperation = Operation.AC
        currentValue = "0"
        previousOperand = null
    }
}

enum class Operation {
    MOD, EQUALLY, AC
}