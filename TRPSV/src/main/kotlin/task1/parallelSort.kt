package task1

import elementNumber
import golem.pow
import mpi.MPI
import task1.HyperCubeCreator.createCartComm
import kotlin.properties.Delegates
import kotlin.system.measureNanoTime

val ROOT: Int = 0
var time: Long = 0

fun parallelTask1(args: Array<String>, size: Int, iterationNumber: Int): MutableList<Long> {
    MPI.Init(args)

    var totalTime = mutableListOf<Long>()

    val comm = MPI.COMM_WORLD
    val procNumber = comm.Size()
    val rank = comm.Rank()
    val N = procNumber.log2().floor()
    val p = (2 pow N).toInt()


    var rootProcess by Delegates.notNull<RootProcess>()
    if (rank == 0) {
        println("N = $N  p = $p")

        rootProcess = task1.rootProcess(size, elementNumber)
    }

    repeat(iterationNumber) {

        MPI.COMM_WORLD.Barrier()

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

    MPI.Finalize()
    return if (rank == 0) totalTime else mutableListOf()
}


fun rootProcess(arraySize: Int, maxValue: Int): RootProcess {
    val array = generateArray(arraySize, maxValue)
    val size = MPI.COMM_WORLD.Size()
    val N = size.log2().floor()

    val rootProcess = RootProcess(N, array)

    return rootProcess
}

fun workProcess() {
    val size = MPI.COMM_WORLD.Size()
    val rank = MPI.COMM_WORLD.Rank()
    val N = size.log2().floor()

    val hyperCube = createCartComm(N)
    val coords = hyperCube.Coords(rank)
    val workProcess = WorkProcess(coords, N, hyperCube)

    workProcess.begin()
}