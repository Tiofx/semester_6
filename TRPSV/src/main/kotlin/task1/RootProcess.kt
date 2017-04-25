package task1

import mpi.MPI
import kotlin.system.measureNanoTime

open class RootProcess(val hyperCubeSize: Int, var array: IntArray) {
    val p = (2 pow hyperCubeSize).toInt()
    private var n = array.size
    val elementsOPerProcess = n / p

    //    val remainElement = n % p
    var timeConsuming = -1L

    fun beginProcess() {
//        println("root\n ${array.task1.str()}")

//        sendOutArray()

//        MPI.COMM_WORLD.Barrier()

        timeConsuming = measureNanoTime {
            sendOutArray()
            collectArray()
        }
//        println(timeConsuming)
//        println("root\n ${array.task1.str()}")
    }

    fun resetArray() {
        shuffle(array)
        timeConsuming = -1L
    }

    //TODO: optimize
    fun sendOutArray() {
//        MPI.COMM_WORLD.Scatter(array, 0, elementsOPerProcess, MPI.INT, array, 0, array.size, MPI.INT, kotlin.getROOT)

//        @Deprecated
        for (i in 0..p - 1) {
//            //TODO: change on scatter?
            MPI.COMM_WORLD.Isend(array, elementsOPerProcess * (i), elementsOPerProcess,
                    MPI.INT, i, Operation.INIT_ARRAY_SEND.ordinal)
        }
    }

    fun collectArray() {
        var offset = 0

        //TODO:delete
//        println("== root start collect")

        for (i in 0..p - 1) {
            val probeStatus = MPI.COMM_WORLD.Probe(i, Operation.COLLECT_ARRAY.ordinal)
            var receiveElementNumber = probeStatus.Get_count(MPI.INT)

            //TODO:delete
//            println("== root receiveElementNumber=$receiveElementNumber")

            //TODO: change on getter
            MPI.COMM_WORLD.Recv(array, offset, n - offset,
                    MPI.INT, i, Operation.COLLECT_ARRAY.ordinal)

            offset += receiveElementNumber

        }
    }
}