import mpi.MPI
import task2.test.*
import task3.plotTask3Result
import task3.testTask3
import visualization.allEdgeProbabilityPlot
import java.io.File
import java.util.*
import kotlin.system.measureNanoTime

//
val elementNumber = 8 * 1e3.toInt()
val iterationNumber = 88

fun task2(args: Array<String>) {
    MPI.Init(args)
    val rank = MPI.COMM_WORLD.Rank()
    MPI.Finalize()

    var makeTest = ResultSet(listOf())
    val measureNanoTime = measureNanoTime { makeTest = makeTest(args, testSet("temp.json")) }
    println(measureNanoTime / 1e9)
    if (rank == 0) {
        allEdgeProbabilityPlot(makeTest)
        println(makeTest.toJson().toJsonString(true))
    }
}

fun task3(args: Array<String>) {
    MPI.Init(args)
    val rank = MPI.COMM_WORLD.Rank()
    MPI.Finalize()

    var testTask3Result = mutableListOf<Triple<Int, Int, ParallelAndSequentialTime>>()
    println(measureNanoTime { testTask3Result = testTask3(args) } / 1e9)

    if (rank == 0) {
        plotTask3Result(testTask3Result)
    }
}

fun main(args: Array<String>) {
    task3(args)

//    val parallelTime = work(args)


//    if (MPI.COMM_WORLD.Rank() == 0) {
//        println(parallelTime
//                .map { it / 1e6 }
//                .map(Any::toString)
//                .reduce { acc, s -> "$acc\n$s" })
//
//        println("""
//            |-------------------------------------------------------------
//            | количество элементов: $kotlin.getElementNumber
//            | количество итераций: $kotlin.getIterationNumber
//            | затраченное время на последовательную реализацию: ${sort(kotlin.getElementNumber, kotlin.getIterationNumber) / 1e6} мс
//            | затраченное время на параллельную реализацию:     ${parallelTime.drop((kotlin.getIterationNumber * 0.1).toInt()).average() / 1e6} мс
//            |-------------------------------------------------------------
//    """.trimMargin())

//    }
}

private fun sort(size: Int, iterationNumber: Int): Double {
    val maxValue = size / 10
    var array = generateArray(size, maxValue)
    var totalSortTime = 0L

    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")

    for (i in 0..iterationNumber - 1) {
        val measureTimeMillis = measureNanoTime { array.quickSort() }
        totalSortTime += measureTimeMillis
//        println(measureTimeMillis)
//        shuffle(array)
        array = generateArray(size, maxValue)
    }

    println("====")

    return totalSortTime / iterationNumber.toDouble()
}

const val filename: String = "array.txt"

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