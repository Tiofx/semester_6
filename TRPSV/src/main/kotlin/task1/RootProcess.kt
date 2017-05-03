package task1

import mpi.MPI

open class RootProcess(val hyperCubeSize: Int, var array: IntArray) {
    val p = (2 pow hyperCubeSize).toInt()
    private var n = array.size
    val elementsOPerProcess = n / p

    fun beginProcess() {
        sendOutArray()
//            collectArray()
    }

    fun resetArray() {
        shuffle(array)
    }

    fun sendOutArray() {
        for (i in 0..p - 1) {
            MPI.COMM_WORLD.Isend(array, elementsOPerProcess * (i), elementsOPerProcess,
                    MPI.INT, i, Operation.INIT_ARRAY_SEND.ordinal)
        }
    }

    fun collectArray() {
        var offset = 0

        for (i in 0..p - 1) {
            val probeStatus = MPI.COMM_WORLD.Probe(i, Operation.COLLECT_ARRAY.ordinal)
            var receiveElementNumber = probeStatus.Get_count(MPI.INT)

            MPI.COMM_WORLD.Recv(array, offset, n - offset,
                    MPI.INT, i, Operation.COLLECT_ARRAY.ordinal)

            offset += receiveElementNumber

        }
    }
}