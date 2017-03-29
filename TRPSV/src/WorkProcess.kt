import mpi.Cartcomm
import mpi.MPI
import kotlin.properties.Delegates

open class WorkProcess(val coords: IntArray, val N: Int, val hyperCube: Cartcomm) {
    protected var array: IntArray by Delegates.notNull<IntArray>()
    protected val pivotContainer = IntArray(1)
    val last = coords.lastIndexOf(1)

    fun begin() {
//        MPI.COMM_WORLD.Barrier()
//        val rootProcess = RootProcess(N, generateArray(elementNumber, elementNumber))

//        val measureTimeMillis = measureTimeMillis {
//            if (hyperCube.Rank(coords) == ROOT) {
//                rootProcess.sendOutArray()
//            }

        receiveArray()
//        return

        //TODO: delete
//        println("workProcess coords=${coords.str()}\n ${array.str()}")


        mainLoop()

//        println("====coor=${coords.str()}\n has array:\n${array.str()}")

        sendOutArray()

//            if (hyperCube.Rank(coords) == ROOT) {
//                rootProcess.collectArray()
//            }
//        }

//        if (hyperCube.Rank(coords) == ROOT) {
//            time = measureTimeMillis
//        }

    }

    protected fun receiveArray() {
//        array = IntArray(2e5.toInt())
//        if (hyperCube.Rank(coords) == ROOT) return
//        MPI.COMM_WORLD.Scatter(array, 0, array.size, MPI.INT, array, 0, array.size, MPI.INT, ROOT)


        val probeStatus = MPI.COMM_WORLD.Probe(ROOT, Operation.INIT_ARRAY_SEND.ordinal)
        var receiveElementNumber = probeStatus.Get_count(MPI.INT)
        array = IntArray(receiveElementNumber)
//
//
        MPI.COMM_WORLD.Recv(array, 0, receiveElementNumber,
                MPI.INT, ROOT, Operation.INIT_ARRAY_SEND.ordinal)
    }

    protected fun sendOutArray() {
        MPI.COMM_WORLD.Issend(array, 0, array.size,
                MPI.INT, ROOT, Operation.COLLECT_ARRAY.ordinal)
    }

    protected fun mainLoop() {
        for (i in 0..N - 1) {
            var pivot = -1

//            println("-$i-coor=${coords.str()} isLead = ${isLeadProcess(i)}")

            if (isLeadProcess(i)) {
                pivot = array.pivotCalculation()

                coords.sendPivot(pivot, i, N)
            } else {
                pivot = receivePivot()
            }

            val numberLessThanPivot = array.sortByPivot(pivot)

            //TODO:delete
//            println("----------\ncoor=${coords.str()}\npivot = $pivot\n numberLessThanPivot = $numberLessThanPivot\narrayAfterPivotSort = ${array.str()}\n------\n")

            coords.exchange(numberLessThanPivot, i)

            //TODO:delete
//            println("-$i-coor=${coords.str()} end of exchange")
//            hyperCube.Barrier()
//            println("-$i-coor=${coords.str()} out of barrier")
        }

//        println("-==-coor=${coords.str()} out of LOOP")

//        array.quickSort()
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

//            if (i != 0 && tempCoords[i - 1] == 1 && this[i - 1] == 0) break

//            println("====coor=${coords.str()}= send pivot to ${hyperCube.Rank(tempCoords)} coors=${tempCoords.str()}")

            MPI.COMM_WORLD.Issend(pivotContainer, 0, 1,
                    MPI.INT, hyperCube.Rank(tempCoords), Operation.PIVOT.ordinal)
        }
    }


    //TODO: change on MPI_Sendrecv_replace
    protected fun IntArray.exchange(numberLessThanPivot: Int, i: Int) {
        //TODO:delete
//        println("--- myRank=${hyperCube.Rank(this)}")


        val greater = this[i] == 1
        this.invert(i)

        val offset = offsetCalculate(greater, numberLessThanPivot)
        val count = countCalculate(greater, numberLessThanPivot, array.size)
        val destinationRank = hyperCube.Rank(this)

        //TODO:delete
//        println("destinationRank=$destinationRank")

        MPI.COMM_WORLD.Issend(array, offset, count,
                MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)

        val probeStatus = MPI.COMM_WORLD.Probe(destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)
        var receiveElementNumber = probeStatus.Get_count(MPI.INT)

        //TODO:delete
//        println("receiveElementNumber=$receiveElementNumber")
//        println("""
//        |~~~$i~~~coor=${coords.str()}
//        |  greater = $greater
//        |  offset = $offset
//        |  count = $count
//        |  destinationRank = $destinationRank
//        |  array = ${array.str()}
//        |  receiveElementNumber = $receiveElementNumber
//        | array.size - count == receiveElementNumber = ${array.size - count == receiveElementNumber}
//        | --------------------------------
//        """.trimMargin())


        //TODO: optimize
        if (array.size - count == receiveElementNumber && false) {
//            val offset = offsetCalculate(!greater, numberLessThanPivot)

            MPI.COMM_WORLD.Recv(array,
                    offset,
                    receiveElementNumber,
                    MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)
        } else {
            val offset = offsetCalculate(!greater, numberLessThanPivot)
            val count = countCalculate(!greater, numberLessThanPivot, array.size)

//            println("""
//            |~~~$i~~~coor=${coords.str()}
//            |  greater = $greater
//            |  offset = $offset
//            |  count = $count
//            |  destinationRank = $destinationRank
//            |  receiveElementNumber = $receiveElementNumber
//            | count + receiveElementNumber = ${count + receiveElementNumber}
//            | =============================================
//        """.trimMargin())


            val array2 = IntArray(count + receiveElementNumber)
            MPI.COMM_WORLD.Recv(array2, 0, receiveElementNumber,
                    MPI.INT, destinationRank, Operation.EXCHANGE_WITH_PROCESS.ordinal)

            for (j in 0..count - 1) {
                array2[j + receiveElementNumber] = array[j + offset]
            }


//            println("~$i~coor=${coords.str()}\n~~~array.size = ${array.size}\n array2.size = ${array2.size}\nreceiveElementNumber=$receiveElementNumber\n-----------")
//            println("~$i~coor=${coords.str()}\n~~array = ${array.str()}\n array2 = ${array2.str()}\n-----------")


            array = array2
        }

//        println("""
//        |~~~$i~~~coor=${coords.str()}
//        |  greater = $greater
//        |  offset = $offset
//        |  count = $count
//        |  destinationRank = $destinationRank
//        |  array = ${array.str()}
//        |  receiveElementNumber = $receiveElementNumber
//        | =============================================
//        """.trimMargin())

        this.invert(i)
    }
}


fun IntArray.next() {
    for (i in this.size - 1 downTo 0) {
        if (this[i] == 0) {
            this[i] = 1
            // TODO: change on lambda with label
            return
        } else {
            this[i] = 0
        }
    }
}

fun IntArray.hasNext(i: Int): Boolean {
    return this.lastIndexOf(0) >= i
}


fun WorkProcess.isLeadProcess(i: Int) = last + 1 <= i

fun countCalculate(greater: Boolean, numberLessThanPivot: Int, size: Int)
        = if (greater) numberLessThanPivot else size - numberLessThanPivot

fun offsetCalculate(greater: Boolean, numberLessThanPivot: Int)
        = if (greater) 0 else numberLessThanPivot