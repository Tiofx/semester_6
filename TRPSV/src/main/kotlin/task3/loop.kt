package task3

import golem.sin
import mpi.MPI
import task2.test.ParallelAndSequentialTime
import java.io.PrintWriter
import java.lang.StringBuilder
import kotlin.system.measureNanoTime


fun testTask3(args: Array<String>) {
    var iSize = 60
    var jSize = 61

    for (i in 0..3) {

//        repeat(1) {
//            val seq = sequentialTask3(iSize, jSize)
//            val parallel = parallelTask3(args, iSize, jSize)
//
//            if (rank(args) == 0) {
//                println("""
//        |iSize: $iSize
//        |jSize: $jSize
//        |sequentialTask3: $seq мс
//        |parallelTask3: $parallel мс
//        |______
//        """.trimMargin())
//
//            }
    }

    iSize *= 2
    jSize *= 2
    jSize -= 1

}

fun rank(args: Array<String>): Int {
    MPI.Init(args)
    val rank = MPI.COMM_WORLD.Rank()
    MPI.Finalize()

    return rank
}

fun measureTask3(args: Array<String>, iSize: Int, jSize: Int, iterationNumber: Int)
        : ParallelAndSequentialTime {

    val parallelResult = parallelTask3(args, iSize, jSize, iterationNumber)

    if (MPI.COMM_WORLD.Rank() == 0) {
        val sequentialResult = sequentialTask3(iSize, jSize, iterationNumber)

        return parallelResult.average() / 1e6 to sequentialResult.average() / 1e6
    }

    return -1.0 to -1.0
}

fun sequentialTask3(iSize: Int, jSize: Int, iterationNumber: Int): MutableList<Long> {
    val result = mutableListOf<Long>()
    val a = DoubleArray2D(iSize, jSize)

    repeat(iterationNumber) {
        val writer = PrintWriter("file.txt")

        val nanoTime = measureNanoTime {
            for (i in 0..iSize - 1) {
                for (j in 0..jSize - 1) {
                    a[i, j] = (10 * i + j).toDouble()
                }
            }

            for (i in 1..iSize - 1) {
                for (j in 0..jSize - 2) {
                    a[i, j] = sin(1e-5 * a[i - 1, j + 1])
                }
            }

            for (i in 0..iSize - 1) {
                for (j in 0..jSize - 1) {
                    writer.write(a[i, j].toString())
                }
                writer.write("\n")
            }
        }

        writer.close()
        a.a.fill(0.0)

        result.add(nanoTime)
    }

    return result
}

fun parallelTask3(args: Array<String>, iSize: Int, jSize: Int, iterationNumber: Int): MutableList<Long> {
    val timeResult = mutableListOf<Long>()
    val a = DoubleArray2D(iSize, jSize)

    MPI.Init(args)

    val comm = MPI.COMM_WORLD
    val rank = comm.Rank()
    val procNumber = comm.Size()

    val temp = DoubleArray(jSize - 1)
    val result = StringBuilder("")
    val perProc = iSize / procNumber

    val startIndex = rank * perProc
    val endIndex = if (rank != procNumber - 1) startIndex + perProc - 1 else iSize - 1

    val perProc2 = (jSize - 1) / procNumber
    val startIndex2 = rank * perProc2 + 1
    val endIndex2 = if (rank != procNumber - 1) startIndex2 + perProc2 - 1 else jSize - 1

    repeat(iterationNumber) {
        var writer = PrintWriter("file.txt")
        writer.close()
        writer = PrintWriter("file.txt")
        MPI.COMM_WORLD.Barrier()

        val nanoTime = measureNanoTime {
            for (i in startIndex..endIndex) {
                for (j in 0..jSize - 1) {
                    a[i, j] = (10 * i + j).toDouble()
                }
            }

            MPI.COMM_WORLD.Allgather(a.a, startIndex * jSize, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE,
                    a.a, 0, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE)

            for (i in 1..iSize - 1) {
                for (j in startIndex2..endIndex2) {
                    a[i, j - 1] = sin(1e-5 * a[i - 1, j])
                }


                MPI.COMM_WORLD.Allgather(a.a, i * (jSize) + startIndex2 - 1, endIndex2 - startIndex2 + 1, MPI.DOUBLE,
                        temp, 0, endIndex2 - startIndex2 + 1, MPI.DOUBLE)
//                    a.a, i * jSize, endIndex2 - startIndex2 + 1, MPI.DOUBLE)

                for (k in temp.indices) {
                    a[i, k] = temp[k]
                }

            }

            for (i in startIndex..endIndex) {
                for (j in 0..jSize - 1) {
                    result.append(a[i, j].toString())
                }
                result.append('\n')
            }


            val charArray = result.toString().toCharArray()
            val charsSize = IntArray(procNumber)

            MPI.COMM_WORLD.Gather(intArrayOf(charArray.size), 0, 1, MPI.INT,
                    charsSize, 0, 1, MPI.INT, 0)

            val recv = if (rank == 0) CharArray(charsSize.sum()) else null
            val intArray = IntArray(procNumber, { (0..(it - 1)).map { charsSize[it] }.sum() })

            MPI.COMM_WORLD.Gatherv(charArray, 0, charArray.count(), MPI.CHAR,
                    recv, 0, charsSize, intArray, MPI.CHAR, 0)

            if (rank == 0) {
                writer.write(recv)
            }

            MPI.COMM_WORLD.Barrier()
        }
        writer.close()

        timeResult.add(nanoTime)
    }

    MPI.Finalize()

    return timeResult
}


class DoubleArray2D(val rowSize: Int, val colSize: Int) {
    val a = DoubleArray(rowSize * colSize)

    inline operator fun get(i: Int, j: Int) = a[i * colSize + j]
    inline operator fun set(i: Int, j: Int, value: Double) {
        a[i * colSize + j] = value
    }
}
