package task2

import mpi.MPI
import task2.graph.*

typealias ParallelAndSequentialTime = Pair<Double, Double>

data class TestSet(val tests: List<TestTask>)
data class ResultSet(val results: List<TestResult>)

data class TestTask(val iterationCoefficient: Double,
                    val graphs: GenerateGraphConfiguration)

data class GenerateGraphConfiguration(val vertexNumber: IntArray,
                                      val edgeProbability: DoubleArray)


data class TestResult(val input: Input,
                      val processNumber: Int,
                      val nanoTime: ParallelAndSequentialTime)

data class Input(val iterationNumber: Int,
                 val vertexNumber: Int,
                 val edgeProbability: Double)


fun makeTest(args: Array<String>, tests: TestSet): ResultSet {
    val processNumber = MPI.COMM_WORLD.Size()

    return tests.tests
            .map {
                (iterationCoefficient, graphs) ->
                val (vertexNumber, edgeProbability) = graphs

                return@map vertexNumber.map {
                    vertexSize ->
                    val iterationNumber = (vertexSize * iterationCoefficient).toInt()

                    edgeProbability.map {
                        edgeProbability ->
                        TestResult(
                                Input(iterationNumber,
                                        vertexSize,
                                        edgeProbability),
                                processNumber,
                                measureTask2(args, iterationNumber, vertexSize, edgeProbability)
                        )
                    }
                }.reduce { acc, testResult -> acc + testResult }
            }
            .reduce { acc, testResult -> acc + testResult }
            .let(::ResultSet)
}


fun measureTask2(args: Array<String>, iterationNumber: Int, vertexNumber: Int, edgeProbability: Double):
        ParallelAndSequentialTime {
    val sourceVertex = random(vertexNumber - 1)
    val maxWeight = INFINITE / (vertexNumber * vertexNumber)

    val inputGraph = InputGraph(adjacencyMatrix(vertexNumber, edgeProbability, maxWeight), sourceVertex)
    val parallelResult = parallelBellmanFord(args, inputGraph, iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialBellmanFord(inputGraph, iterationNumber)

        return parallelResult.average() to sequentialResult.average()
    }

    return -1.0 to -1.0
}



