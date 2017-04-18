package task2

import iterationNumber
import mpi.MPI
import task2.parallel.Work
import task2.parallel.WorkMaster
import kotlin.system.measureNanoTime

fun task2(args: Array<String>) {
    val vertexNumber = 333
    val edgeProbability = 0.9
    val maxWeight = 99999

    val inputGraph = InputGraph(random(vertexNumber - 1), adjacencyMatrix(vertexNumber, edgeProbability, maxWeight))

    val parallelResult = parallelBellmanFord(args, inputGraph, iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialBellmanFord(inputGraph, iterationNumber)

        //TODO: delete
        println(parallelResult.map { it / 1e6 })
        println(sequentialResult.map { it / 1e6 })

        println("""
            |Количество процессов: ${MPI.COMM_WORLD.Size()}
            |Количество итераций: $iterationNumber
            |=============================================
            |Параллельный алгоритм:
            |${parallelResult.map { it }.drop(3).average() / 1e6} мс
            |====
            |Последовательный алгоритм:
            |${sequentialResult.map { it }.average() / 1e6} мс
            """.trimMargin())

    }
}

fun sequentialBellmanFord(inputGraph: InputGraph, iterationNumber: Int): MutableList<Long> {
    val result = mutableListOf<Long>()

    repeat(iterationNumber) {
        val nanoTime = measureNanoTime {
            bellmanFord(inputGraph)
        }

        result.add(nanoTime)
    }

    return result
}

fun parallelBellmanFord(args: Array<String>, inputGraph: InputGraph, iterationNumber: Int): MutableList<Long> {
    MPI.Init(args)
    val comm = MPI.COMM_WORLD
    val rank = comm.Rank()

    val workMaster = WorkMaster(inputGraph)
    val result = mutableListOf<Long>()

    repeat(iterationNumber) {
        comm.Barrier()

        val nanoTime = measureNanoTime {
            when (rank) {
                0 -> workMaster.work()
                else -> Work().work()
            }
        }

        if (rank == 0) result.add(nanoTime)

        comm.Barrier()
    }

    MPI.Finalize()
    return result
}