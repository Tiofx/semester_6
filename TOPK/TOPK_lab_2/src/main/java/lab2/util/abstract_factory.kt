package lab2.util

import lab1.main.FiniteStateAutomaton.ERROR_CODE
import lab2.main.Automation
import lab2.util.variantNative.Code
import lab2.util.variantNative.Code.*

interface Alphabet {
    val data: Map<Char, Int>
}

interface State {
    val stateNumber: Int
}

interface FinalStates {
    val setOfFinalStates: Set<Int>
}

interface TransitionTable {
    val table: Array<IntArray>
}


class Lab1Alphabet : Alphabet {
    override val data by lazy {
        hashMapOf('a' to 0,
                'b' to 1,
                'c' to 2
        )
    }
}

class Lab1FinalStates : FinalStates {
    override val setOfFinalStates by lazy { setOf(3, 6, 7, 8) }
}

class Lab1TransitionTable : TransitionTable {
    override val table by lazy {
        arrayOf(intArrayOf(1, ERROR_CODE, 1, ERROR_CODE, 5, ERROR_CODE, 7, 7, 5),
                intArrayOf(ERROR_CODE, 2, ERROR_CODE, ERROR_CODE, ERROR_CODE, 6, 6, ERROR_CODE, ERROR_CODE),
                intArrayOf(8, ERROR_CODE, 3, 3, ERROR_CODE, ERROR_CODE, 7, 7, 3))
    }
}

class ZeroState : State {
    override val stateNumber: Int = 0
}


class Lab2Alphabet : Alphabet {
    override val data by lazy {
        hashMapOf('P' to 0,
                'R' to 1,
                'O' to 2,
                'C' to 3,
                'E' to 4,
                'N' to 5,
                'D' to 6,
                '0' to 9,
                '1' to 10,
                '=' to 11,
                ',' to 12,
                '&' to 13,
                '!' to 14,
                '<' to 15,
                '>' to 16,
                '+' to 17,
                '-' to 18,
                '.' to 19,
                ' ' to 20,
                '\n' to 21,
                '\t' to 22
        )
    }
}

class Lab2FinalStates : FinalStates {
    override val setOfFinalStates by lazy {
        setOf(PROC, END, IDEN, DATA,
                Sign.EQUALLY, Sign.COMMA, Sign.DOUBLE_AMPERSAND,
                Sign.DOUBLE_EXCLAMATION_POINT, Sign.LEFT_SHIFT, Sign.RIGHT_SHIFT,
                Code.Error.L3, Code.Error.IN_CHAIN, Code.Error.IN_KEY_WORD, Code.Error.IN_VARIABLE,
                Code.Error.IN_CONSTANT, Code.Error.IN_DOUBLE_AMPERSAND, Code.Error.IN_DOUBLE_EXCLAMATION_POINT,
                Code.Error.IN_LEFT_SHIFT, Code.Error.IN_RIGHT_SHIFT
        )
    }
}

class Lab2TransitionTable(filename: String) : TransitionTable {
    override val table by lazy { Parser.toArray(filename) }
}

abstract class AbstractAutomationFactory {

    abstract fun createAlphabet(): Alphabet

    abstract fun createFirstState(): State

    abstract fun createFinalStates(): FinalStates

    abstract fun createTransitionTable(): TransitionTable
}

class AutomationFactoryLab1 : AbstractAutomationFactory() {

    override fun createAlphabet() = Lab1Alphabet()

    override fun createFirstState() = ZeroState()

    override fun createFinalStates() = Lab1FinalStates()

    override fun createTransitionTable() = Lab1TransitionTable()

}

class AutomationFactoryLab2 : AbstractAutomationFactory() {

    protected val filename = "table.txt"

    override fun createAlphabet() = Lab2Alphabet()

    override fun createFirstState() = ZeroState()

    override fun createFinalStates() = Lab2FinalStates()

    override fun createTransitionTable() = Lab2TransitionTable(filename)

}

fun AbstractAutomationFactory.createAutomation() =
        Automation(
                createAlphabet().data,
                createTransitionTable().table,
                createFirstState().stateNumber,
                createFinalStates().setOfFinalStates
        )

