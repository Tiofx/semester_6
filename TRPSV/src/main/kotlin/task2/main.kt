package task2

import mpi.MPI
import task2.test.GenerateValues
import task2.test.parallelBellmanFord
import task2.test.sequentialBellmanFord

fun task2(args: Array<String>) {
    val vertexNumber = 999
    val edgeProbability = 0.9
    val iterationNumber = 22

    val parallelResult = parallelBellmanFord(args, GenerateValues(vertexNumber, edgeProbability), iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialBellmanFord(GenerateValues(vertexNumber, edgeProbability), iterationNumber)

        //TODO: delete
        println(parallelResult.map { it / 1e6 })
        println(sequentialResult.map { it / 1e6 })

        println("""
            |Количество процессов: ${MPI.COMM_WORLD.Size()}
            |Количество итераций: $iterationNumber
            |=============================================
            |Параллельный алгоритм:
            |${parallelResult.drop((iterationNumber * 0.1).toInt()).average() / 1e6} мс
            |====
            |Последовательный алгоритм:
            |${sequentialResult.average() / 1e6} мс
            """.trimMargin())
    }
}

//fun sequentialBellmanFord(inputGraph: InputGraph, iterationNumber: Int): MutableList<Long> {
//    val result = mutableListOf<Long>()
//    val (adjacencyMatrix, sourceVertex, vertexNumber) = inputGraph
//    val plainAdjacencyList = adjacencyMatrix.toPlainAdjacencyList()
//
//    repeat(iterationNumber) {
//        val nanoTime = measureNanoTime {
//            bellmanFord(plainAdjacencyList, sourceVertex, vertexNumber)
//        }
//        result.add(nanoTime)
//    }
//
//    return result
//}
//
//fun parallelBellmanFord(args: Array<String>, inputGraph: InputGraph, iterationNumber: Int):
//        MutableList<Long> {
//    MPI.Init(args)
//
//    val comm = MPI.COMM_WORLD
//    val rank = comm.Rank()
//
//    val process = if (rank == 0) WorkMaster(inputGraph) else Work()
//    val result = mutableListOf<Long>()
//
//    repeat(iterationNumber) {
//        comm.Barrier()
//
//        val nanoTime = measureNanoTime {
//            process.task1.parallelTask1()
//        }
//
////        if (rank == 0) {
////            println(process.distance contentEquals bellmanFord(inputGraph))
////        }
//
//
//        if (rank == 0) result.add(nanoTime)
//
//        process.reset()
//        comm.Barrier()
//    }
//
//    MPI.Finalize()
//    return result
//}


