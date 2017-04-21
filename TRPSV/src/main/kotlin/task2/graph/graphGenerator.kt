package task2.graph

import task2.graph.Util.AdjacencyMatrixUtil.toAdjacencyList
import task2.graph.Util.AdjacencyMatrixUtil.toPlainAdjacencyList

private inline operator fun AdjacencyMatrix.set(range: IntRange, value: Int) =
        range.forEach { this[it][it] = value }

fun plainAdjacencyList(vertexNumber: Int,
                       edgeProbability: Double,
                       maxWeight: Int = INFINITE / (vertexNumber * vertexNumber)): PlainAdjacencyList =
        adjacencyMatrix(vertexNumber, edgeProbability, maxWeight)
                .toPlainAdjacencyList()

fun adjacencyList(vertexNumber: Int, edgeProbability: Double, maxWeight: Int): AdjacencyList =
        adjacencyMatrix(vertexNumber, edgeProbability, maxWeight)
                .toAdjacencyList()

fun adjacencyMatrix(vertexNumber: Int,
                    edgeProbability: Double = 0.5,
                    maxWeight: Int = INFINITE / (vertexNumber * (vertexNumber + 1))): AdjacencyMatrix =
        Array(vertexNumber, { IntArray(vertexNumber, { generateWeight(edgeProbability, maxWeight) }) })
                .apply { this[0..vertexNumber - 1] = 0 }


private fun generateWeight(edgeProbability: Double, maxWeight: Int) =
        if (Math.random() <= edgeProbability) random(maxWeight) else NO_EDGE

fun random(maxValue: Int) = (1 + Math.random() * maxValue).toInt()


fun AdjacencyMatrix.toReadableString() = this
        .map { it.map { if (it == task2.graph.INFINITE) "x" else it.toString() }.reduce { acc, s -> "$acc $s" }.trim() }
        .reduce { acc, ints -> "$acc\n$ints" }
        .trim()

fun AdjacencyList.toReadableString() = this
        .map { it.toString() }
        .reduce { acc, s -> "$acc\n$s" }
        .trim()