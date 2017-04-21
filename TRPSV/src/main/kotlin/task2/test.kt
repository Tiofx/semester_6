package task2

import mpi.MPI
import task2.graph.PlainAdjacencyList
import task2.graph.plainAdjacencyList

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

data class GenerateValues(val vertexNumber: Int,
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
                                measureTask2(args, iterationNumber, GenerateValues(vertexSize, edgeProbability))
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

        return parallelResult.average() to sequentialResult.average()
    }

    return -1.0 to -1.0
}


fun GenerateValues.generateGraph(): PlainAdjacencyList =
        plainAdjacencyList(vertexNumber, edgeProbability)