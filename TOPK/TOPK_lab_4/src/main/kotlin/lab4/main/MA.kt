package lab4.main

import getFileFromResources
import lab2.util.variantNative.Code
import lab4.util.LexemeCode
import lab4.util.LexemeCode.PROGRAM_BEGIN
import lab4.util.LexemeCode.STACK_PUSH
import lab4.util.simplePrecedence.Code.EQUALL
import lab4.util.simplePrecedence.Code.LEFT_OPERATION
import lab4.util.simplePrecedence.Code.RIGHT_OPERATION
import lab4.util.simplePrecedence.Code.SUCESS
import lab4.util.simplePrecedence.Error
import toArrayIntArray
import toList

open class MA(val rowColMap: Map<Int, Int>,
              val rules: List<Pair<Int, List<Int>>>,
              val table: Array<IntArray>) {

    protected val magazine = MutableList<Int>(1, { _ -> STACK_PUSH })
    protected var currentPoint = 0
    protected var lastHead = 0
    var code = -1

    var log = -1
    val stackLog = MutableList<List<Int>>(0, { emptyList() })

    fun reset() {
        currentPoint = 0
        code = -1
        log = -1
        stackLog.clear()

        magazine.clear()
        magazine.safeSet(0, STACK_PUSH)
    }

    fun sendAll(sequence: List<Int>) {
        reset()

        try {
            sequence.forEach {
                code = it
                makeStep()
                if (log != -1)
                    return
            }

            code = STACK_PUSH
            makeStep()
        } catch (e: Exception) {
            log = Error.RULE_NOT_FOUND
        }
    }

    protected fun makeStep() {
        stackLog.add(magazine.slice(0..currentPoint))

        val row = rowColNum(magazine[currentPoint])
        val col = rowColNum(code)

        when (table[row][col]) {
            LEFT_OPERATION -> {
                currentPoint++
                magazine.safeSet(currentPoint, LEFT_OPERATION)
                currentPoint++
                magazine.safeSet(currentPoint, code)
                lastHead = currentPoint
            }

            EQUALL -> {
                currentPoint++
                magazine.safeSet(currentPoint, code)
            }

            RIGHT_OPERATION -> {
                val ruleNum = findRule()

                if (table[rowColNum(magazine[lastHead - 2])]
                        [rowColNum(ruleNum)]
                        == LEFT_OPERATION) {
                    magazine.safeSet(lastHead, ruleNum)
                    currentPoint = lastHead
                } else {
                    lastHead--
                    magazine.safeSet(lastHead, ruleNum)
                    currentPoint = lastHead
                    lastHead = findLastHead() + 1

                    if (table[rowColNum(magazine[currentPoint - 1])]
                            [rowColNum(ruleNum)] in Error.PROGRAM_STRUCTURE_OUT..Error.SIGN_COMBINATION) {
                        log = table[rowColNum(magazine[currentPoint - 1])][rowColNum(ruleNum)]
                        stackLog.add(magazine.slice(0..currentPoint))
                        return
                    }
                }


                makeStep()
            }

            in Error.PROGRAM_STRUCTURE_OUT..Error.SIGN_COMBINATION -> {
                log = table[row][col]
                stackLog.add(magazine.slice(0..currentPoint) + code)
            }

            SUCESS -> log = SUCESS

            else -> log = Error.UNKNOWN
        }
    }

    protected fun rowColNum(code: Int): Int = rowColMap[code]!!

    protected fun findRule(): Int {
        for ((code, list) in rules) {
            if (list.size != currentPoint - lastHead + 1) {
                continue
            }

            if ((lastHead..currentPoint).none { list[it - lastHead] != magazine[it] }) {
                return code
            }
        }

        throw Exception()
    }

    protected fun findLastHead(): Int {
        var lastHead = currentPoint

        while (magazine[lastHead] != LEFT_OPERATION) {
            lastHead--
        }

        return lastHead
    }
}

fun <E> MutableList<E>.safeSet(index: Int, element: E) {
    try {
        this[index] = element
    } catch (e: IndexOutOfBoundsException) {
        if (index < 0) {
            this.add(0, element)
        } else {
            this.add(element)
        }
    }
}

fun createMyAutomation(): MA {
    val diagnosticTable = "lab_4_table_for_MA.txt".getFileFromResources()

    val rowColMap = mapOf(
            LexemeCode.FRAGMENT to 0,
            LexemeCode.PROGRAM_BEGIN to 1,
            LexemeCode.PROGRAM_BODY to 2,
            LexemeCode.OPERATOR to 3,
            LexemeCode.OPERATOR_END to 4,
            LexemeCode.OPERAND to 5,
            LexemeCode.SIGN to 6,
            Code.PROC to 7,
            Code.END to 8,
            Code.IDEN to 9,
            Code.DATA to 10,
            Code.Sign.COMMA to 11,
            Code.Sign.EQUALLY to 12,
            Code.Sign.DOUBLE_AMPERSAND to 13,
            Code.Sign.DOUBLE_EXCLAMATION_POINT to 14,
            Code.Sign.LEFT_SHIFT to 15,
            Code.Sign.RIGHT_SHIFT to 16,
            STACK_PUSH to 17
    )

    val rules = listOf(
            LexemeCode.FRAGMENT to listOf(PROGRAM_BEGIN, LexemeCode.PROGRAM_BODY),
            LexemeCode.PROGRAM_BEGIN to listOf(Code.PROC, Code.Sign.COMMA),
            LexemeCode.PROGRAM_BODY to listOf(LexemeCode.OPERATOR, LexemeCode.PROGRAM_BODY),
            LexemeCode.PROGRAM_BODY to listOf(Code.END, Code.Sign.COMMA),
            LexemeCode.OPERATOR to listOf(Code.IDEN, Code.Sign.EQUALLY, LexemeCode.OPERATOR_END),
            LexemeCode.OPERATOR_END to listOf(LexemeCode.OPERAND, LexemeCode.SIGN, LexemeCode.OPERATOR_END),
            LexemeCode.OPERATOR_END to listOf(LexemeCode.OPERAND, Code.Sign.COMMA),
            LexemeCode.OPERAND to listOf(Code.IDEN),
            LexemeCode.OPERAND to listOf(Code.DATA),
            LexemeCode.SIGN to listOf(Code.Sign.DOUBLE_AMPERSAND),
            LexemeCode.SIGN to listOf(Code.Sign.DOUBLE_EXCLAMATION_POINT),
            LexemeCode.SIGN to listOf(Code.Sign.LEFT_SHIFT),
            LexemeCode.SIGN to listOf(Code.Sign.RIGHT_SHIFT)
    )

    val table = diagnosticTable.toArrayIntArray()

    return MA(rowColMap, rules, table)
}

fun main(args: Array<String>) {
    val testCodes = "test_lab_4_codes.txt".getFileFromResources()

    val ma = createMyAutomation()

    ma.sendAll(testCodes.toList())

    println(ma.log)
    println(ma.translateStackLog())
}