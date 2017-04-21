package task2.graph

val INFINITE = Int.MAX_VALUE
val NO_EDGE = INFINITE

typealias AdjacencyMatrix = Array<IntArray>
typealias AdjacencyMatrix1D = IntArray
typealias AdjacencyList = Array<Adjacency>
typealias PlainAdjacencyList = IntArray
typealias Adjacency = Triple<Int, Int, Int>


data class InputGraph(val adjacencyMatrix: AdjacencyMatrix,
                      val sourceVertex: Int,
                      val vertexNumber: Int = adjacencyMatrix.size)

enum class PlainAdjacency(val number: Int) {
    SOURCE(0), DESTINATION(1), WEIGHT(2)

}

object Util {
    object AdjacencyUtil {
        val Adjacency.source: Int
            get() = this.first

        val Adjacency.destination: Int
            get() = this.second

        val Adjacency.weight: Int
            get() = this.third
    }

    object AdjacencyMatrixUtil {
        inline fun AdjacencyMatrix.vertexNumber() = this.size
        inline fun AdjacencyMatrix.toIntArray(): AdjacencyMatrix1D = this.reduce { acc, ints -> acc + ints }

        fun AdjacencyMatrix.toPlainAdjacencyList(): PlainAdjacencyList =
                this.mapIndexed { rowNum, row ->
                    row.mapIndexed { colNum, weight -> if (weight != INFINITE) intArrayOf(rowNum, colNum, weight) else null }
                            .filterNotNull()
                            .reduce { acc, ints -> acc + ints }
                }
                        .reduce { acc, list -> acc + list }

        fun AdjacencyMatrix.toAdjacencyList() = this.mapIndexed { row, ints ->
            ints.mapIndexed { col, w -> if (w != INFINITE) Triple(row, col, w) else null }
                    .filterNotNull()
        }
                .reduce { acc, list -> acc + list }
                .toTypedArray()
    }

    object AdjacencyMatrix1DUtil {
        inline fun AdjacencyMatrix1D.toAdjacencyMatrix(rowColNum: Int = Math.sqrt(this.size.toDouble()).toInt()): AdjacencyMatrix
                = Array(rowColNum, { this.copyOfRange(it * rowColNum, (it + 1) * rowColNum) })
    }

    object AdjacencyListUtil {
        val AdjacencyList.edgeNumber: Int
            get() = this.size

        inline fun AdjacencyList.toIntArray(): PlainAdjacencyList =
                this.map { it.toList().toIntArray() }.reduce { acc, ints -> acc + ints }

    }

    object PlainAdjacencyListUtil {
        inline operator fun PlainAdjacencyList.get(index: Int, col: Int) = this[3 * index + col]

        inline operator fun PlainAdjacencyList.get(index: Int, content: PlainAdjacency) = this[index, content.number]

        val PlainAdjacencyList.edgeNumber: Int
            get() = (this.size + 1) / 3

        inline fun PlainAdjacencyList.toAdjacencyList(): AdjacencyList = this
                .mapIndexed { index, value -> index to value }
                .groupBy({ it.first / 3 }, { it.second })
                .map { (_, value) -> Triple(value[0], value[1], value[2]) }
                .toTypedArray()
    }
}
