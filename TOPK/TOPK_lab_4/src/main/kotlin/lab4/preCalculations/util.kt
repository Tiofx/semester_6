package lab4.preCalculations

import golem.eachIndexed
import golem.matrix.Matrix
import golem.set

val F_VALUE = 1 shl 0
val EQ_VALUE = 1 shl 1
val L_VALUE = 1 shl 2

fun Matrix<Double>.toF(): Matrix<Double> {
    return this.toValue(lab4.preCalculations.F_VALUE)
}

fun Matrix<Double>.toEQ(): Matrix<Double> {
    return this.toValue(lab4.preCalculations.EQ_VALUE)
}

fun Matrix<Double>.toL(): Matrix<Double> {
    return this.toValue(lab4.preCalculations.L_VALUE)
}

private fun Matrix<Double>.toValue(value: Int): Matrix<Double> {
    val copy = this.copy()

    this.forEachIndexed { index, d ->
        copy[index] =
                (if (d.toInt() and value > 0) 1 else 0).toDouble()
    }

    return copy
}


fun Matrix<Double>.warshall(): Matrix<Double> {
    val matrixPlus = this.copy()

    matrixPlus.eachIndexed(fun(i: Int, j: Int, d: Double) {
        if (matrixPlus[j, i] > 0) {
            for (k in 0..matrixPlus.numCols() - 1) {
                matrixPlus[j, k] = matrixPlus[j, k].toInt() or matrixPlus[i, k].toInt()
            }
        }
    })

    return matrixPlus
}