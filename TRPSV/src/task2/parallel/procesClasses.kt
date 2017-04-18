package task2.parallel

import mpi.MPI
import task2.*
import kotlin.properties.Delegates
import kotlin.system.measureNanoTime

abstract class AbstractProcess {
    abstract var vertexNumber: Int
    abstract var sourceVertex: Int
    abstract var adjacencyList: AdjacencyList
    abstract var adjacencyMatrix: AdjacencyMatrix
    abstract val isRoot: Boolean

    var distance by Delegates.notNull<IntArray>()


    val rank: Int = MPI.COMM_WORLD.Rank()
    val procNum = MPI.COMM_WORLD.Size()
    var edgeSegment by Delegates.notNull<EdgeSegment>()

    protected var temp by Delegates.notNull<IntArray>()
    protected var adjacencyList1D by Delegates.notNull<AdjacencyList1D>()
    protected var adjacencyMatrix1D by Delegates.notNull<AdjacencyMatrix1D>()


    fun work() {
        println("preparation: ${measureNanoTime { preparation() } / 1e6}\n" +
                "mainWork:    ${measureNanoTime { mainWork() } / 1e6}\n__")
    }

    abstract protected fun mainWork()
    abstract protected fun preparation()
}

class WorkMaster(override var vertexNumber: Int,
                 override var sourceVertex: Int,
                 override var adjacencyMatrix: AdjacencyMatrix) : Work() {
    override val isRoot = true

    constructor(graph: InputGraph) : this(graph.vertexNumber, graph.sourceVertex, graph.adjacencyMatrix)

    override fun preparation() {
        adjacencyList = adjacencyMatrix.toAdjacencyList()
        adjacencyMatrix1D = adjacencyMatrix.toIntArray()
        super.preparation()
    }
}

open class Work : AbstractProcess() {
    override var vertexNumber: Int = -1
    override var sourceVertex: Int = 0
    override var adjacencyMatrix by Delegates.notNull<AdjacencyMatrix>()
    override var adjacencyList by Delegates.notNull<AdjacencyList>()
    override val isRoot = false

    override fun mainWork() {
        for (i in 0..vertexNumber - 1) {
            val hasRelax = adjacencyList.relaxAll(distance, edgeSegment.startEdge, edgeSegment.endEdge)
            MPI.COMM_WORLD.Allreduce(distance, 0, temp, 0, vertexNumber, MPI.INT, MPI.MIN)

            if (!hasRelax and (distance contentEquals temp)) break

            distance = temp.clone()
        }

    }

    override fun preparation() {
        vertexNumber = mpiBcastOneValue(vertexNumber, MPI.INT, 0)
        sourceVertex = mpiBcastOneValue(sourceVertex, MPI.INT, 0)

        distance = IntArray(vertexNumber, { INFINITE })
        distance[sourceVertex] = 0
        temp = IntArray(vertexNumber)

        if (!isRoot) adjacencyMatrix1D = AdjacencyMatrix1D(vertexNumber * vertexNumber)
        MPI.COMM_WORLD.Bcast(adjacencyMatrix1D, 0, vertexNumber * vertexNumber, MPI.INT, 0)
//        println("--Bcast: ${measureNanoTime { MPI.COMM_WORLD.Bcast(adjacencyMatrix1D, 0, vertexNumber * vertexNumber, MPI.INT, 0) } / 1e6} мс")
        if (!isRoot) adjacencyList = adjacencyMatrix1D.toAdjacencyMatrix().toAdjacencyList()

        edgeSegment = EdgeSegment()
    }
}

