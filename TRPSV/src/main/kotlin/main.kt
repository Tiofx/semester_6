import task2.test.makeTest
import task2.test.testSet
import visualization.allEdgeProbabilityPlot
import java.io.File
import java.util.*
import kotlin.system.measureNanoTime

//
val elementNumber = 8 * 1e3.toInt()
val iterationNumber = 88


//val kotlin.getElementNumber = 32
//val kotlin.getIterationNumber = 1
//
fun main(args: Array<String>) {
//    val parallelTime = work(args)

//    testTask3(args)
    allEdgeProbabilityPlot(makeTest(args, testSet("temp.json")))

//    MPI.Init(args)
//    val rank = MPI.COMM_WORLD.Rank()
//    MPI.Finalize()
//
//    val makeTest = makeTest(args, testSet("temp.json"))
//    if (rank == 0) {
//        val x = makeTest.results.map { it.input.vertexNumber }.toIntArray()
//        val y1 = makeTest.results.map { it.millisecondTime.first }.toDoubleArray()
//        val y2 = makeTest.results.map { it.millisecondTime.second }.toDoubleArray()
//
//        figure(1)
//        plot(x, y1, lineLabel = "Параллельный алгоритм")
//        plot(x, y2, "b", lineLabel = "Последовательный алгоритм")
//        ylabel("Время, мс")
//        xlabel("Количество вершин")
//
//        println(makeTest.toJson().toJsonString(true))
//
//        makeTest.results.groupBy { it.input.edgeProbability }
//    }

//    task2(args)

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