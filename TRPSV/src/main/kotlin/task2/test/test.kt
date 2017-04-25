package task2.test

import mpi.MPI
import task2.graph.bellmanFord.bellmanFord
import task2.graph.bellmanFord.parallel.Work
import task2.graph.bellmanFord.parallel.WorkMaster
import task2.graph.random
import kotlin.system.measureNanoTime


fun iterationNumber(iterationCoefficient: Int, vertexNumber: Int) =
        maxOf(3, (iterationCoefficient * 1e4 / vertexNumber).toInt())

fun makeTest(args: Array<String>, tests: TestSet): ResultSet {
    MPI.Init(args)
    val processNumber = MPI.COMM_WORLD.Size()
    MPI.Finalize()

    return tests.tests
            .map {
                (iterationCoefficient, graphs) ->
                val (vertexNumber, edgeProbability) = graphs

                return@map vertexNumber.map {
                    vertexNumber ->
                    val iterationNumber = iterationNumber(iterationCoefficient, vertexNumber)

                    edgeProbability.map {
                        edgeProbability ->
                        TestResult(
                                processNumber,
                                Input(iterationNumber,
                                        vertexNumber,
                                        edgeProbability),
                                measureTask2(args, iterationNumber, GenerateValues(vertexNumber, edgeProbability))
                        )
                    }
                }.reduce { acc, testResult -> acc + testResult }
            }
            .reduce { acc, testResult -> acc + testResult }
            .let(::ResultSet)
}


fun measureTask2(args: Array<String>, iterationNumber: Int, generateValues: GenerateValues):
        ParallelAndSequentialTime {

    val parallelResult = parallelBellmanFord(args, generateValues, iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialBellmanFord(generateValues, iterationNumber)

        return parallelResult.average() / 1e6 to
                sequentialResult.average() / 1e6

//        return parallelResult.drop(maxOf(3, (iterationNumber * 0.1).toInt())).average() / 1e6 to
//                sequentialResult.drop(maxOf(3, (iterationNumber * 0.1).toInt())).average() / 1e6
    }

    return -1.0 to -1.0
}


fun sequentialBellmanFord(generateValues: GenerateValues, iterationNumber: Int): MutableList<Long> {
    val result = mutableListOf<Long>()

    repeat(iterationNumber) {
        val plainAdjacencyList = generateValues.generateGraph()
        val sourceVertex = random(generateValues.vertexNumber - 1)
        val vertexNumber = generateValues.vertexNumber

        val nanoTime = measureNanoTime {
            bellmanFord(plainAdjacencyList, sourceVertex, vertexNumber)
        }

        result.add(nanoTime)
    }

    return result
}

fun parallelBellmanFord(args: Array<String>, generateValues: GenerateValues, iterationNumber: Int):
        MutableList<Long> {
    MPI.Init(args)

    val comm = MPI.COMM_WORLD
    val rank = comm.Rank()

    val process = if (rank == 0) WorkMaster(generateValues) else Work()
    val result = mutableListOf<Long>()

    repeat(iterationNumber) {
        comm.Barrier()

        val nanoTime = measureNanoTime {
            process.work()
        }

        if (rank == 0) {
            result.add(nanoTime)
            process.plainAdjacencyList = generateValues.generateGraph()
        }

        process.reset()
        comm.Barrier()
    }

    MPI.Finalize()
    return result
}
