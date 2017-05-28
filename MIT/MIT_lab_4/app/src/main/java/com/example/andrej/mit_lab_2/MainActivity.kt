package com.example.andrej.mit_lab_2

import android.widget.TextView

open class Calculator(val textView: TextView, var numberSystem: Int = 10) {
    var currentValue = "0"
        set(value) {
            field = value
            textView.text = field
        }

    open protected var previousOperand: String? = null
    open protected var currentOperation = Operation.AC
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

    open protected fun calculate(operation: Operation) =
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
    MOD, EQUALLY, AC, PLUS, MINUS
}