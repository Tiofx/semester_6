package task1

import filename
import mpi.MPI
import java.io.File
import java.util.*


enum class Operation {
    SORT_AROUND_PIVOT, EXCHANGE_WITH_PROCESS, SORT, INIT_ARRAY_SEND, COLLECT_ARRAY, PIVOT
}

fun Double.floor() = Math.floor(this).toInt()
fun Double.ceil() = Math.ceil(this)
infix fun Int.pow(pow: Int) = Math.pow(this.toDouble(), pow.toDouble())
fun Int.log2() = Math.log(this.toDouble()) / Math.log(2.0)

fun IntArray.invert(i: Int) {
    this[i] = if (this[i] == 0) 1 else 0
}

fun generateArray(size: Int, maxValue: Int): IntArray {
    val numbers = IntArray(size)
    val list = File(filename).readLines()
            .flatMap { it.split(Regex("[ \t\n]")) }
            .filter(String::isNotBlank)
            .map(String::toInt)

    if (list.isEmpty() || list[0] != size) {
        val generator = Random()

        for (i in numbers.indices) {
            numbers[i] = generator.nextInt(maxValue)
        }
        return numbers

//        File(filename).writeText(
//                "$size\n" +
//                        numbers.map(Any::toString)
//                                .reduceRight { s, acc -> "$acc $s" }
//        )

//        return numbers
    } else {
        (0..size - 1).forEach { numbers[it] = list[it + 1] }

        return numbers
    }

}

fun shuffle(array: IntArray) {
    val n = array.size
    for (i in array.indices) {
        val random = i + (Math.random() * (n - i)).toInt()

        val randomElement = array[random]
        array[random] = array[i]
        array[i] = randomElement
    }
}


object HyperCubeCreator {
    fun createCartComm(N: Int) =
            MPI.COMM_WORLD.Create_cart(
                    createDims(N),
                    createPeriods(N),
                    createReorder()
            )

    fun createDims(N: Int) = IntArray(N, { _ -> 2 })
    fun createPeriods(N: Int) = BooleanArray(N, { _ -> false })
    fun createReorder() = false
}

fun IntArray.pivotCalculation() = this.average().floor()

fun IntArray.sortByPivot(pivot: Int): Int {
    var j = -1
    for (i in 0..this.size - 1) {
        if (this[i] <= pivot) {
            j++
            this.swap(i, j)
        }
    }

    j++
    return j
}

fun IntArray.str() = if (this.isNotEmpty()) this.map(Int::toString).reduce { acc, s -> "$acc $s" } else "emptyArray"