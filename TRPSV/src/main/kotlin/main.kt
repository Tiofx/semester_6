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

//    val parallelTime = task1.parallelTask1(args)


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
//            | затраченное время на последовательную реализацию: ${sequentialTask1(kotlin.getElementNumber, kotlin.getIterationNumber) / 1e6} мс
//            | затраченное время на параллельную реализацию:     ${parallelTime.drop((kotlin.getIterationNumber * 0.1).toInt()).average() / 1e6} мс
//            |-------------------------------------------------------------
//    """.trimMargin())

//    }
}

const val filename: String = "array.txt"
