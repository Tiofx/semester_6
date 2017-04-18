package task2

val INFINITE = Int.MAX_VALUE
val NO_EDGE = INFINITE

typealias AdjacencyMatrix = Array<IntArray>
typealias AdjacencyMatrix1D = IntArray
typealias AdjacencyList = Array<Adjacency>
typealias AdjacencyList1D = IntArray
typealias Adjacency = Triple<Int, Int, Int>


val Adjacency.source: Int
    get() = this.first

val Adjacency.destination: Int
    get() = this.second

val Adjacency.weight: Int
    get() = this.third


inline fun AdjacencyMatrix.vertexNumber() = this.size

inline fun AdjacencyMatrix.toIntArray(): AdjacencyMatrix1D = this.reduce { acc, ints -> acc + ints }

inline fun AdjacencyMatrix1D.toAdjacencyMatrix(rowColNum: Int = Math.sqrt(this.size.toDouble()).toInt()): AdjacencyMatrix
        = Array(rowColNum, { this.copyOfRange(it * rowColNum, (it + 1) * rowColNum) })


val AdjacencyList.edgeNumber: Int
    get() = this.size

inline fun AdjacencyList.toIntArray(): AdjacencyList1D =
        this.map { it.toList().toIntArray() }.reduce { acc, ints -> acc + ints }

inline fun AdjacencyList1D.toAdjacencyList(): AdjacencyList = this
        .mapIndexed { index, value -> index to value }
        .groupBy({ it.first / 3 }, { it.second })
        .map { (_, value) -> Triple(value[0], value[1], value[2]) }
        .toTypedArray()


data class InputGraph(val sourceVertex: Int,
                      val adjacencyMatrix: AdjacencyMatrix,
                      val vertexNumber: Int = adjacencyMatrix.size)