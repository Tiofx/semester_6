package lab4.preCalculations

import golem.matrix.Matrix
import toIntMatrixString
import toPrecedenceString

data class CalculationSteps(val table: Matrix<Double>,
                            val first: Matrix<Double>, val equivalence: Matrix<Double>, val last: Matrix<Double>,
                            val fPlus: Matrix<Double>, val lPlus: Matrix<Double>,
                            val leftOperation: Matrix<Double>, val rightOperation: Matrix<Double>,
                            val combine: Matrix<Double>) {

    override fun toString(): String {
        return """
                |совмещенная карта отношений F, L, EQ:
                |${table.toIntMatrixString()}
                |------------------------------------
                |матрица отношений 'начинается на'(F):
                |${first.toIntMatrixString()}
                |------------------------------------
                |матрица отношений 'соседние символы'(EQ):
                |${equivalence.toIntMatrixString()}
                |------------------------------------
                |матрица отношений 'заканчивается на'(L):
                |${last.toIntMatrixString()}
                |------------------------------------
                |====================================
                |транзитивное замыкание F+:
                |${fPlus.toIntMatrixString()}
                |------------------------------------
                |транзитивное замыкание L+:
                |${lPlus.toIntMatrixString()}
                |------------------------------------
                |====================================
                |матрица отношения <•:
                |-- (EQ * F+)
                |${leftOperation.toIntMatrixString()}
                |------------------------------------
                |матрица отношения •>:
                |-- (L+)T * EQ * (I + F+)
                |${rightOperation.toIntMatrixString()}
                |------------------------------------
                |матрица отношений ≡:
                |${equivalence.toIntMatrixString()}
                |------------------------------------
                |====================================
                |матрица отношения предшествования:
                |${combine.toIntMatrixString()}
                |====================================
                |${combine.toPrecedenceString()}
    """.trimMargin()
    }
}