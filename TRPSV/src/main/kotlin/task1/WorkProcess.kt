package task1

import mpi.Cartcomm
import mpi.MPI
import kotlin.properties.Delegates

open class WorkProcess(val coords: IntArray, val N: Int, val hyperCube: Cartcomm) {
    protected var array: IntArray by Delegates.notNull<IntArray>()
    protected val pivotContainer = IntArray(1)
    val last = coords.lastIndexOf(1)

    private var numberElement = 0
    private var tempArray = IntArray(0)

    fun begin() {
        receiveArray()
        mainLoop()
//        sendOutArray()
    }

    protected fun receiveArray() {
        val probeStatus = MPI.COMM_WORLD.Probe(ROOT, Operation.INIT_ARRAY_SEND.ordinal)
        var receiveElementNumber = probeStatus.Get_count(MPI.INT)
        array = IntArray(receiveElementNumber)

        val status = MPI.COMM_WORLD.Recv(array, 0, receiveElementNumber,
                MPI.INT, ROOT, Operation.INIT_ARRAY_SEND.ordinal)

        numberElement = status.Get_count(MPI.INT)
        tempArray = IntArray(N * numberElement)
    }

    protected fun sendOutArray() {
        MPI.COMM_WORLD.Isend(array, 0, array.size,
                MPI.INT, ROOT, Operation.COLLECT_ARRAY.ordinal)
    }

    protected fun mainLoop() {
        for (i in 0..N - 1) {
            var pivot = -1

            if (isLeadProcess(i)) {
                pivot = array.pivotCalculation()

                coords.sendPivot(pivot, i, N)
            } else {
                pivot = receivePivot()
            }

            val numberLessThanPivot = array.sortByPivot(pivot)
            coords.exchange(numberLessThanPivot, i)
        }

        array.sort()
    }

    protected fun receivePivot(): Int {
        MPI.COMM_WORLD.Recv(pivotContainer, 0, 1,
                MPI.INT, MPI.ANY_SOURCE, Operation.PIVOT.ordinal)
        return pivotContainer[0]
    }

    protected fun IntArray.sendPivot(pivot: Int, i: Int, N: Int) {
        val tempCoords = this.clone()
        pivotContainer[0] = pivot

        while (tempCoords.hasNext(i)) {
            tempCoords.next()

            MPI.COMM_WORLD.Isend(pivotContainer, 0, 1,
                    MPI.INT, hyperCube.Rank(tempCoords), Operation.PIVOT.ordinal)
        }
    }


    protected fun IntArray.exchange(numberLessThanPivot: Int, i: Int) {
        val greater = this[i] == 1
        this.invert(i)

        val offset = offsetCalculate(greater, numberLessThanPivot)
        val count = countCalculate(greater, numberLessThanPivot, array.size)
        val destinationRank = hyperCube.Rank(this)

        MPI.COMM_WORLD.Isend(array, offset, count,
                MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)

        val probeStatus = MPI.COMM_WORLD.Probe(destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)
        var receiveElementNumber = probeStatus.Get_count(MPI.INT)

        val offset2 = offsetCalculate(!greater, numberLessThanPivot)
        val count2 = countCalculate(!greater, numberLessThanPivot, array.size)


        val array2 = IntArray(count2 + receiveElementNumber)
        MPI.COMM_WORLD.Recv(array2, 0, receiveElementNumber,
                MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)

        for (j in 0..count2 - 1) {
            array2[j + receiveElementNumber] = array[j + offset2]
        }
        array = array2

        this.invert(i)
    }
}


fun IntArray.next() {
    for (i in this.size - 1 downTo 0) {
        if (this[i] == 0) {
            this[i] = 1
            return
        } else {
            this[i] = 0
        }
    }
}

inline fun IntArray.hasNext(i: Int) = this.lastIndexOf(0) >= i


inline fun WorkProcess.isLeadProcess(i: Int) = last + 1 <= i

inline fun countCalculate(greater: Boolean, numberLessThanPivot: Int, size: Int)
        = if (greater) numberLessThanPivot else size - numberLessThanPivot

inline fun offsetCalculate(greater: Boolean, numberLessThanPivot: Int)
        = if (greater) 0 else numberLessThanPivot