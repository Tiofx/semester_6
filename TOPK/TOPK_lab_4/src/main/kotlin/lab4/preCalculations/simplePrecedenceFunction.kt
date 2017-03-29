package lab4.preCalculations

import golem.eye
import golem.mapRowsToList
import golem.matrix.Matrix
import golem.zeros
import toIntMatrixString

data class SimplePrecedenceFunctionCalculation(
        val calculation: CalculationSteps,
        val G_E: Matrix<Double> = calculation.equivalence * calculation.rightOperation,
        val L_E: Matrix<Double> = calculation.equivalence * calculation.leftOperation,
        val B: Matrix<Double> = calculateB(
                calculation.leftOperation,
                calculation.rightOperation,
                calculation.equivalence
        ),
        val BDash: Matrix<Double> = calculateBDash(B),
        val functions: PrecedenceFunctions = calculateFunction(BDash)) {

    override fun toString() = """
    |G E
    |${G_E.toIntMatrixString()}
    |------------------------------------
    |L E
    |${L_E.toIntMatrixString()}
    |------------------------------------
    |B
    |${B.toIntMatrixString()}
    |------------------------------------
    |B*
    |${BDash.toIntMatrixString()}
    |------------------------------------
    |F
    |${functions.F}
    |------------------------------------
    |G
    |${functions.G}
    |------------------------------------
    """.trimMargin()
}

data class PrecedenceFunctions(val F: List<Int>, val G: List<Int>)

fun calculateFunction(bDash: Matrix<Double>): PrecedenceFunctions {
    val rowSum = bDash.mapRowsToList { it.sum() }
            .map(Double::toInt)

    return PrecedenceFunctions(rowSum.subList(0, rowSum.size / 2 - 1), rowSum.subList(rowSum.size / 2, rowSum.size - 1))
}

fun calculateBDash(B: Matrix<Double>) = eye(B.numRows()) + B.warshall()

fun calculateB(leftOperation: Matrix<Double>, rightOperation: Matrix<Double>, equivalence: Matrix<Double>):
        Matrix<Double> {
    val sizeB = 2 * leftOperation.numRows()

    val B = zeros(sizeB)

    B[0..sizeB / 2 - 1, sizeB / 2..sizeB - 1] = equivalence * rightOperation
    B[sizeB / 2..sizeB - 1, 0..sizeB / 2 - 1] = (equivalence * leftOperation).T

    return B
}
