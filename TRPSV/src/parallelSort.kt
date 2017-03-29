import HyperCubeCreator.createCartComm
import mpi.MPI

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

fun work(args: Array<String>): MutableList<Long> {
    MPI.Init(args)

    var totalTime = mutableListOf<Long>()

    val comm = MPI.COMM_WORLD
    val size = comm.Size()
    val rank = comm.Rank()
    val N = size.log2().floor()
    val p = (2 pow N).toInt()

    if (rank == 0) {
        println("N = $N  p = $p")
    }

//    println(rank)

    for (i in 0..iterationNumber - 1) {

        if (rank == 0) Thread({ rootProcess(elementNumber, elementNumber) }).start()

        when (rank) {
//        0 -> rootProcess()
            in 0..p -> workProcess()
//        in 1..p -> workProcess()
            else -> println("The process with rank=$rank is too lazy to work!")
        }

        if (rank == 0) {
            totalTime.add(time)
//            totalTime += time
//            println("time = $time    totalTime = $totalTime")
        } else {
//            MPI.COMM_WORLD.Barrier()
        }

    }


    MPI.Finalize()

    return if (rank == 0) totalTime else mutableListOf(-1)
//    println("totalTime=$totalTime iterationNumber = $iterationNumber")
}


fun rootProcess(arraySize: Int, maxValue: Int) {
    val array = generateArray(arraySize, maxValue)
    val size = MPI.COMM_WORLD.Size()
    val N = size.log2().floor()


    val rootProcess = RootProcess(N, array)
    rootProcess.beginProcess()

    time = rootProcess.timeConsuming
//    rootProcess.timeConsuming = 0

//    MPI.COMM_WORLD.Barrier()
}

fun workProcess() {
    val size = MPI.COMM_WORLD.Size()
    val rank = MPI.COMM_WORLD.Rank()
    val N = size.log2().floor()


    val hyperCube = createCartComm(N)


    val coords = hyperCube.Coords(rank)
//
//    val workProcess = WorkProcessDebug(coords, N, hyperCube)
    val workProcess = WorkProcess(coords, N, hyperCube)
    workProcess.begin()
}

object HyperCubeCreator {
    fun createCartComm(N: Int) =
            mpi.MPI.COMM_WORLD.Create_cart(
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