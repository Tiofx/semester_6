package task1

import elementNumber
import generateArray
import mpi.MPI
import task1.HyperCubeCreator.createCartComm
import kotlin.properties.Delegates
import kotlin.system.measureNanoTime

val ROOT: Int = 0

enum class Operation {
    SORT_AROUND_PIVOT, EXCHANGE_WITH_PROCESS, SORT, INIT_ARRAY_SEND, COLLECT_ARRAY, PIVOT
}

fun Double.floor() = Math.floor(this).toInt()
fun Double.ceil() = Math.ceil(this)
infix fun Int.pow(pow: Int) = Math.pow(this.toDouble(), pow.toDouble())
fun Int.log2() = Math.log(this.toDouble()) / Math.log(2.0)


fun IntArray.invert(i: Int) {
    this[i] = if (this[i] == 0) 1 else 0
}

var time: Long = 0

fun parallelTask1(args: Array<String>, iterationNumber: Int): Double {
    MPI.Init(args)

    var totalTime = mutableListOf<Long>()

    val comm = MPI.COMM_WORLD
    val size = comm.Size()
    val rank = comm.Rank()
    val N = size.log2().floor()
    val p = (2 pow N).toInt()


    var rootProcess by Delegates.notNull<RootProcess>()
    if (rank == 0) {
        println("N = $N  p = $p")

        rootProcess = task1.rootProcess(elementNumber, elementNumber)
    }

//    val start = MPI.Wtick()

    repeat(iterationNumber) {

        MPI.COMM_WORLD.Barrier()

//        if (rank == 0) Thread({ task1.rootProcess(kotlin.getElementNumber, kotlin.getElementNumber) }).start()

        totalTime.add(
                measureNanoTime {
                    if (rank == 0) {
                        rootProcess.sendOutArray()
                    }

                    when (rank) {
                        in 0..p -> workProcess()
                        else -> println("The process with rank=$rank is too lazy to task1.parallelTask1!")
                    }

                    if (rank == 0) {
                        rootProcess.collectArray()
                    }
                }
        )

        if (rank == 0) rootProcess.resetArray()
        MPI.COMM_WORLD.Barrier()
    }

//    val end = MPI.Wtick()

//    println("$rank:  ${(end - start)} ${(end - start).toDouble() / iterationNumber}")

    MPI.Finalize()

    return if (rank == 0) totalTime.average() / 1e6 else -1.0
}


fun rootProcess(arraySize: Int, maxValue: Int): RootProcess {
    val array = generateArray(arraySize, maxValue)
    val size = MPI.COMM_WORLD.Size()
    val N = size.log2().floor()

    val rootProcess = RootProcess(N, array)

    return rootProcess

//    task1.rootProcess.beginProcess()
//    kotlin.getTime = task1.rootProcess.timeConsuming
//    task1.rootProcess.timeConsuming = 0

//    MPI.COMM_WORLD.Barrier()
}

fun workProcess() {
    val size = MPI.COMM_WORLD.Size()
    val rank = MPI.COMM_WORLD.Rank()
    val N = size.log2().floor()


    val hyperCube = createCartComm(N)


    val coords = hyperCube.Coords(rank)
//
//    val task1.workProcess = WorkProcessDebug(coords, N, hyperCube)
    val workProcess = WorkProcess(coords, N, hyperCube)

//    if (rank != 0) MPI.COMM_WORLD.Barrier()

    workProcess.begin()
}

object HyperCubeCreator {
    fun createCartComm(N: Int) =
            MPI.COMM_WORLD.Create_cart(
                    createDims(N),
                    createPeriods(N),
                    createReorder()
            )

    fun createDims(N: Int) = IntArray(N, { _ -> 2 })
    fun createPeriods(N: Int) = BooleanArray(N, { _ -> false })
    fun createReorder() = false
}

fun IntArray.pivotCalculation() = this.average().floor()

fun IntArray.sortByPivot(pivot: Int): Int {
    var j = -1
    for (i in 0..this.size - 1) {
        if (this[i] <= pivot) {
            j++
            this.swap(i, j)
        }
    }

    j++
    return j
}

fun IntArray.str() = if (this.isNotEmpty()) this.map(Int::toString).reduce { acc, s -> "$acc $s" } else "emptyArray"