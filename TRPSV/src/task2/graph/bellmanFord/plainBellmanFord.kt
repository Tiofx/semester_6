package task2.graph.bellmanFord

import task2.graph.INFINITE
import task2.graph.InputGraph
import task2.graph.PlainAdjacency
import task2.graph.PlainAdjacencyList
import task2.graph.Util.AdjacencyMatrixUtil.toPlainAdjacencyList
import task2.graph.Util.PlainAdjacencyListUtil.edgeNumber
import task2.graph.Util.PlainAdjacencyListUtil.get

inline fun bellmanFord(graph: InputGraph) = with(graph) {
    bellmanFord(
            adjacencyMatrix.toPlainAdjacencyList(),
            sourceVertex,
            vertexNumber)
}


fun bellmanFord(plainAdjacencyList: PlainAdjacencyList,
                sourceVertex: Int,
                vertexNumber: Int): IntArray {

    val distance = IntArray(vertexNumber, { INFINITE }).apply { this[sourceVertex] = 0 }

    while (plainAdjacencyList.relaxAll(distance)) {
    }
    return distance
}

inline fun PlainAdjacencyList.relaxAll(distance: IntArray, from: Int = 0, to: Int = edgeNumber - 1) =
        (from..to).map { relax(it, distance) }
                .onEach { if (it) return@relaxAll true }
                .let { false }


fun PlainAdjacencyList.relax(index: Int, distance: IntArray): Boolean {
    val lastValue = distance[get(index, PlainAdjacency.DESTINATION)]

    if (distance[get(index, PlainAdjacency.SOURCE)] < INFINITE) {
        distance[get(index, PlainAdjacency.DESTINATION)] =
                minOf(distance[get(index, PlainAdjacency.DESTINATION)].toLong(),
                        distance[get(index, PlainAdjacency.SOURCE)].toLong()
                                + get(index, PlainAdjacency.WEIGHT))
                        .toInt()
    }

    val isRelaxed = lastValue != distance[get(index, PlainAdjacency.DESTINATION)]

    return isRelaxed
}