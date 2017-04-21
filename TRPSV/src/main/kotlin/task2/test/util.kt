package task2.test

import task2.graph.PlainAdjacencyList
import task2.graph.plainAdjacencyList

typealias ParallelAndSequentialTime = Pair<Double, Double>

data class TestSet(val tests: List<TestTask>)
data class ResultSet(val results: List<TestResult>)

data class TestTask(val iterationCoefficient: Int,
                    val graphs: GenerateGraphConfiguration)

data class GenerateGraphConfiguration(val vertexNumber: IntArray,
                                      val edgeProbability: DoubleArray)

data class TestResult(val processNumber: Int,
                      val input: Input,
                      val millisecondTime: ParallelAndSequentialTime)

data class Input(val iterationNumber: Int,
                 val vertexNumber: Int,
                 val edgeProbability: Double)

data class GenerateValues(val vertexNumber: Int,
                          val edgeProbability: Double)

fun GenerateValues.generateGraph(): PlainAdjacencyList =
        plainAdjacencyList(vertexNumber, edgeProbability)