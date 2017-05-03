package task1

import mpi.MPI
import task2.test.ParallelAndSequentialTime
import task3.rank
import kotlin.system.measureNanoTime

fun iterationNumber(size: Int, parameter: Int = 1e6.div(3).toInt()) = maxOf(20, parameter / size)
//fun iterationNumber(size: Int, parameter: Int = 1e5.div(3).toInt()) = maxOf(10, parameter / size)

fun testTask1(args: Array<String>): MutableList<Pair<Int, ParallelAndSequentialTime>> {
    var size = 1000
//    val times = 11
    val times = 11
    val result = mutableListOf<Pair<Int, ParallelAndSequentialTime>>()

    repeat(times) {
        val iterationNumber = iterationNumber(size)
        val measureTask3 = measureTask1(args, size, iterationNumber)

        if (rank(args) == 0) {

//            println("""
//            |size: $size
//            |iterationNumber: $iterationNumber
//            |sequentialTask1: ${measureTask3.second} мс
//            |parallelTask1: ${measureTask3.first} мс
//            |______
//        """.trimMargin())

            result.add(size to measureTask3)
        }

//        size = (1.5 * size).toInt()
        size += if (size > 1e5.toInt()) 15e4.toInt() else size
    }

    return result
}

fun measureTask1(args: Array<String>, size: Int, iterationNumber: Int)
        : ParallelAndSequentialTime {

    val parallelResult = parallelTask1(args, size, iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialTask1(size, iterationNumber)

//        println("""
//        |sequentialResult = ${sequentialResult.map { it / 1e6 }.toString()}
//        |parallelResult = ${parallelResult.map { it / 1e6 }.toString()}
//        """.trimMargin())
        return parallelResult.average() / 1e6 to sequentialResult.average() / 1e6
    }

    return -1.0 to -1.0
}

fun sequentialTask1(size: Int, iterationNumber: Int): MutableList<Long> {
    val maxValue = size / 10
    var array = generateArray(size, maxValue)
    var totalTime = mutableListOf<Long>()


    repeat(iterationNumber) {
        val measureTimeMillis = measureNanoTime { array.quickSort() }
        totalTime.add(measureTimeMillis)

        shuffle(array)
//        array = generateArray(size, maxValue)
    }

//    println("====")

    return totalTime
}