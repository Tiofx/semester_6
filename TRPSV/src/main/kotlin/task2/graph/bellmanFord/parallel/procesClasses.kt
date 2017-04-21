package task2.graph.bellmanFord.parallel

import mpi.MPI
import task2.graph.AdjacencyMatrix
import task2.graph.INFINITE
import task2.graph.InputGraph
import task2.graph.PlainAdjacencyList
import task2.graph.Util.AdjacencyMatrixUtil.toPlainAdjacencyList
import task2.graph.Util.PlainAdjacencyListUtil.edgeNumber
import task2.graph.bellmanFord.relaxAll
import kotlin.properties.Delegates
import kotlin.system.measureNanoTime

class WorkMaster(override var vertexNumber: Int,
                 override var sourceVertex: Int,
                 override var plainAdjacencyList: PlainAdjacencyList) : Work() {
    override val isRoot = true
    override var edgeNumber: Int = -1
        get() = plainAdjacencyList.edgeNumber

    constructor(graph: InputGraph) : this(graph.vertexNumber, graph.sourceVertex, graph.adjacencyMatrix)

    constructor(vertexNumber: Int, sourceVertex: Int, adjacencyMatrix: AdjacencyMatrix)
            : this(vertexNumber, sourceVertex, adjacencyMatrix.toPlainAdjacencyList())
}

open class Work : AbstractProcess() {
    override var vertexNumber: Int = -1
    override var edgeNumber: Int = -1
    override var sourceVertex: Int = 0
    override var plainAdjacencyList by Delegates.notNull<PlainAdjacencyList>()

    override val isRoot = false

    override protected fun mainWork() {
        var hasRelax = true

        for (i in 0..vertexNumber - 1) {
            println("--- ${measureNanoTime { hasRelax = plainAdjacencyList.relaxAll(distance, edgeSegment.startEdge, edgeSegment.endEdge) } / 1e6}")
//            hasRelax = plainAdjacencyList.relaxAll(distance, edgeSegment.startEdge, edgeSegment.endEdge)

//            println("-AllReduce-- ${measureNanoTime { MPI.COMM_WORLD.Allreduce(distance, 0, temp, 0, vertexNumber, MPI.INT, MPI.MIN) } / 1e6}")
            MPI.COMM_WORLD.Allreduce(distance, 0, temp, 0, vertexNumber, MPI.INT, MPI.MIN)

            if (!hasRelax and (distance contentEquals temp)) break

            distance = temp.clone()
//            println("--- ${measureNanoTime { } / 1e6}")
        }
    }

    override protected fun preparation() {
        vertexNumber = mpiBcastOneValue(vertexNumber, MPI.INT, 0)
        edgeNumber = mpiBcastOneValue(edgeNumber, MPI.INT, 0)
        sourceVertex = mpiBcastOneValue(sourceVertex, MPI.INT, 0)

        try {
            distance.size
        } catch (e: IllegalStateException) {
            firstPreparation()
        }

        distance[sourceVertex] = 0
        MPI.COMM_WORLD.Bcast(plainAdjacencyList, 0, 3 * edgeNumber, MPI.INT, 0)
        edgeSegment = EdgeSegment()
    }

    protected open fun firstPreparation() {
        distance = IntArray(vertexNumber, { INFINITE })
        temp = IntArray(vertexNumber)

        if (!isRoot) plainAdjacencyList = PlainAdjacencyList(3 * edgeNumber)
    }
}

abstract class AbstractProcess {
    abstract var vertexNumber: Int
    abstract var edgeNumber: Int
    abstract var sourceVertex: Int

    abstract var plainAdjacencyList: PlainAdjacencyList

    abstract val isRoot: Boolean
    var distance by Delegates.notNull<IntArray>()

    val rank: Int = MPI.COMM_WORLD.Rank()
    val procNum = MPI.COMM_WORLD.Size()

    var edgeSegment by Delegates.notNull<EdgeSegment>()
    protected var temp by Delegates.notNull<IntArray>()

    fun work() {
        println("""
        |${measureNanoTime { preparation() } / 1e6} мс
        |${measureNanoTime { mainWork() } / 1e6} мс
        |____
        """.trimMargin())
//        preparation()
//        mainWork()
    }

    fun reset() {
        distance.fill(INFINITE)
        temp.fill(0)
    }

    abstract protected fun mainWork()

    abstract protected fun preparation()
}
