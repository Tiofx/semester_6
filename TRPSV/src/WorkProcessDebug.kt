import mpi.Cartcomm
import mpi.MPI
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

open class WorkProcessDebug(val coords: IntArray, val N: Int, val hyperCube: Cartcomm) {
    protected var array: IntArray by Delegates.notNull<IntArray>()
    protected val pivotContainer = IntArray(1)
    val last = coords.lastIndexOf(1)

    fun begin() {
//        MPI.COMM_WORLD.Barrier()
        var rootProcess: RootProcess? = null
        rootProcess = RootProcess(N, generateArray(elementNumber, elementNumber))
        if (hyperCube.Rank(coords) == ROOT) {
            println("${rootProcess?.array?.str()}")
        }


        val measureTimeMillis = measureTimeMillis {

            array = kotlin.IntArray(rootProcess!!.elementsOPerProcess)

            MPI.COMM_WORLD.Scatter(rootProcess?.array, 0, array.size, MPI.INT, array, 0, array.size, MPI.INT, ROOT)

//            receiveArray()
            mainLoop()
//            sendOutArray()
            println(array.size)
            println(array.str())

            MPI.COMM_WORLD.Gather(array, 0, array.size, MPI.INT, rootProcess?.array, 0, array.size, MPI.INT, ROOT)
//            MPI.COMM_WORLD.Gatherv(array, 0, array.size, MPI.INT, rootProcess?.array, 0, , MPI.INT, ROOT)
        }



        if (hyperCube.Rank(coords) == ROOT) {
            println("${rootProcess?.array?.str()}")
            time = measureTimeMillis
        }
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

    private fun isLeadProcess(i: Int) = last + 1 <= i

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


        if (array.size - count == receiveElementNumber && false) {

            MPI.COMM_WORLD.Recv(array,
                    offset,
                    receiveElementNumber,
                    MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)
        } else {
            val offset = offsetCalculate(!greater, numberLessThanPivot)
            val count = countCalculate(!greater, numberLessThanPivot, array.size)

            val array2 = IntArray(count + receiveElementNumber)
            MPI.COMM_WORLD.Recv(array2, 0, receiveElementNumber,
                    MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)

            for (j in 0..count - 1) {
                array2[j + receiveElementNumber] = array[j + offset]
            }


            array = array2
        }

        this.invert(i)
    }
}
