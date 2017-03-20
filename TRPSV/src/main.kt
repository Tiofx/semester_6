import mpi.MPI
import java.util.*
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val elementNumber = 1e5.toInt()
    val iterationNumber = 1e1.toInt()

    MPI.Init(args)
    val comm = MPI.COMM_WORLD

    println("""
            |-------------------------------------------------------------
            |ранг: ${comm.Rank()}
            | количество элементов: $elementNumber
            | количество итераций: $iterationNumber
            | затраченное время: ${sort(elementNumber, iterationNumber)} мс
            |-------------------------------------------------------------
    """.trimMargin())

    MPI.Finalize()
}

private fun sort(size: Int, interationNumber: Int): Double {
    val maxValue = size / 10
    val array = generateArray(size, maxValue)
    var totalSortTime = 0L

    for (i in 0..interationNumber - 1) {
        totalSortTime += measureTimeMillis { array.quickSort() }
        shuffle(array)
    }

    return totalSortTime / interationNumber.toDouble()
}

fun generateArray(size: Int, maxValue: Int): IntArray {
    val numbers = IntArray(size)
    val generator = Random()

    for (i in numbers.indices) {
        numbers[i] = generator.nextInt(maxValue)
    }

    return numbers
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