package task2

private inline operator fun AdjacencyMatrix.set(range: IntRange, value: Int) =
        range.forEach { this[it][it] = value }

fun plainAdjacencyList(vertexNumber: Int, edgeProbability: Double, maxWeight: Int): PlainAdjacencyList =
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
        .map { it.map { if (it == task2.INFINITE) "x" else it.toString() }.reduce { acc, s -> "$acc $s" }.trim() }
        .reduce { acc, ints -> "$acc\n$ints" }
        .trim()

fun AdjacencyList.toReadableString() = this
        .map { it.toString() }
        .reduce { acc, s -> "$acc\n$s" }
        .trim()


fun test() {
    val source = 0
    val vertexNumber = 10

    val matrix = adjacencyMatrix(vertexNumber, 0.3, 9)
    println("""
            |${matrix.toReadableString()}
            |_
            |{matrix.toIntArray().contentToString()}
            |_
            |{matrix.toIntArray().toAdjacencyMatrix(vertexNumber).toReadableString()}
            |_
            |{bellmanFord(matrix.toAdjacencyList(), source, vertexNumber).contentToString()}
            |_
            |${matrix.toAdjacencyList().toReadableString()}
            |_
            |${matrix.toAdjacencyList().toIntArray().contentToString()}
            |_
            |${matrix.toAdjacencyList().toIntArray().toAdjacencyList().toReadableString()}
    """.trimMargin())
}