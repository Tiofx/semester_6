import task1.plotTask1Result
import task1.testTask1
import task2.test.*
import task3.plotTask3Result
import task3.rank
import task3.testTask3
import visualization.allEdgeProbabilityPlot
import kotlin.system.measureNanoTime

//
val elementNumber = 8 * 1e3.toInt()
val iterationNumber = 88

fun task1(args: Array<String>) {
    val rank = rank(args)

    var task1 = mutableListOf<Pair<Int, ParallelAndSequentialTime>>()
    println(measureNanoTime { task1 = testTask1(args) } / 1e9)

    if (rank == 0) {
        plotTask1Result(task1)
    }
}

fun task2(args: Array<String>) {
    val rank = rank(args)

    var makeTest = ResultSet(listOf())
    val measureNanoTime = measureNanoTime { makeTest = makeTest(args, testSet("temp.json")) }
    println(measureNanoTime / 1e9)

    if (rank == 0) {
        allEdgeProbabilityPlot(makeTest)
        println(makeTest.toJson().toJsonString(true))
    }
}

fun task3(args: Array<String>) {
    val rank = rank(args)

    var testTask3Result = mutableListOf<Triple<Int, Int, ParallelAndSequentialTime>>()
    println(measureNanoTime { testTask3Result = testTask3(args) } / 1e9)

    if (rank == 0) {
        plotTask3Result(testTask3Result)
    }
}

fun main(args: Array<String>) {
    task1(args)
//    task2(args)
//    task3(args)
}

const val filename: String = "array.txt"
