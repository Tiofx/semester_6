package lab4.preCalculations

import adjacencyListToMatrix
import golem.eachIndexed
import golem.eye
import golem.matrix.Matrix
import golem.set
import lab4.util.simplePrecedence.Code
import java.io.File

class SimplePrecedenceRelationsException : Exception()


fun combineSimplePrecedence(equivalence: Matrix<Double>, leftOperation: Matrix<Double>, rightOperation: Matrix<Double>):
        Matrix<Double> {
    val combineResult = leftOperation.copy()

    equivalence.eachIndexed { i, j, value ->
        combineResult[i, j] = when {
            value != 0.0 -> Code.EQUALL
            rightOperation[i, j] != 0.0 -> Code.RIGHT_OPERATION
            leftOperation[i, j] != 0.0 -> Code.LEFT_OPERATION
            else -> 0
        }
    }

    return combineResult
}

fun checkSimplePrecedence(equivalence: Matrix<Double>, leftOperation: Matrix<Double>, rightOperation: Matrix<Double>) {
    equivalence.eachIndexed { i, j, value ->
        if (listOf<Int>(value.toInt(),
                leftOperation[i, j].toInt(),
                rightOperation[i, j].toInt())
                .count { it != 0 } > 1)
            throw SimplePrecedenceRelationsException()
    }
}

fun calculateMatrix(filename: File): CalculationSteps {
    val table = adjacencyListToMatrix(filename)
    val size = table.numRows()

    val first = table.toF()
    val equivalence = table.toEQ()
    val last = table.toL()

    val fPlus = first.warshall()
    val lPlus = last.warshall()

    val leftOperation = equivalence * fPlus
    val rightOperation = lPlus.T * equivalence * (eye(size) + fPlus)

    checkSimplePrecedence(equivalence, leftOperation, rightOperation)

    val combine = combineSimplePrecedence(equivalence, leftOperation, rightOperation)

    return CalculationSteps(table,
            first, equivalence, last,
            fPlus, lPlus,
            leftOperation, rightOperation,
            combine)
}
