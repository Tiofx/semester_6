package task3

import golem.sin
import mpi.MPI
import task2.test.ParallelAndSequentialTime
import java.io.PrintWriter
import java.lang.StringBuilder
import kotlin.system.measureNanoTime

//fun iterationNumber(iSize: Int, jSize: Int, parameter: Int = 1e6.div(3).toInt()) = maxOf(15, parameter / (iSize * jSize))
fun iterationNumber(iSize: Int, jSize: Int, parameter: Int = 1e6.div(3).toInt()) = 15

fun testTask3(args: Array<String>): MutableList<Triple<Int, Int, ParallelAndSequentialTime>> {
    var iSize = 60
    var jSize = 61
    val testNum = 6
    val result = mutableListOf<Triple<Int, Int, ParallelAndSequentialTime>>()

    for (i in 0..testNum) {
        val iterationNumber = iterationNumber(iSize, jSize)
        val measureTask3 = measureTask3(args, iSize, jSize, iterationNumber)

        if (rank(args) == 0) {

            println("""
            |iSize: $iSize
            |jSize: $jSize
            |iterationNumber: $iterationNumber
            |sequentialTask3: ${measureTask3.second} мс
            |parallelTask3: ${measureTask3.first} мс
            |______
        """.trimMargin())
            result.add(Triple(iSize, jSize, measureTask3))
        }

//        iSize += 100
//        jSize += 100

//        iSize *= 2
        jSize *= 2
        jSize -= 1
    }

    return result
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

        println("""
        |sequentialResult = ${sequentialResult.map { it / 1e6 }.toString()}
        |parallelResult = ${parallelResult.map { it / 1e6 }.toString()}
        """.trimMargin())
        return parallelResult.average() / 1e6 to sequentialResult.average() / 1e6
    }

    return -1.0 to -1.0
}

fun sequentialTask3(iSize: Int, jSize: Int, iterationNumber: Int): MutableList<Long> {
    val result = mutableListOf<Long>()
    val a = DoubleArray2D(iSize, jSize)

    repeat(iterationNumber) {
        var writer = PrintWriter("file.txt")
        writer.write("")
        writer.close()
        writer = PrintWriter("file.txt")

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
                    writer.append(a[i, j].toString())
//                    writer.append("${a[i, j]}")
                }
                writer.append("\n")
            }
        }

        writer.close()
        a.a.fill(0.0)

        result.add(nanoTime)
    }

    return result
}

var recv: CharArray? = null

fun parallelTask3(args: Array<String>, iSize: Int, jSize: Int, iterationNumber: Int): MutableList<Long> {
    val timeResult = mutableListOf<Long>()
    val a = DoubleArray2D(iSize, jSize)

    MPI.Init(args)

    val comm = MPI.COMM_WORLD
    val rank = comm.Rank()
    val procNumber = comm.Size()

//    var recv: CharArray? = null
    val temp = DoubleArray(jSize - 1)
    val result = StringBuilder("")
    val perProc = iSize / procNumber

    val startIndex = rank * perProc
    val endIndex = if (rank != procNumber - 1) startIndex + perProc - 1 else iSize - 1

    val perProc2 = (jSize - 1) / procNumber
    val startIndex2 = rank * perProc2 + 1
    val endIndex2 = if (rank != procNumber - 1) startIndex2 + perProc2 - 1 else jSize - 1


    var nanoTime2 = 0.0

    repeat(iterationNumber) {
        var writer = PrintWriter("file.txt")
        writer.write("")
        writer.close()
        writer = PrintWriter("file.txt")
        MPI.COMM_WORLD.Barrier()

        val nanoTime = measureNanoTime {
//            for (i in startIndex..endIndex) {
//                for (j in 0..jSize - 1) {
//                    a[i, j] = (10 * i + j).toDouble()
//                }
//            }
//
//            MPI.COMM_WORLD.Allgather(a.a, startIndex * jSize, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE,
//                    a.a, 0, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE)

            for (i in 1..iSize - 1) {
                for (j in startIndex2..endIndex2) {
                    a[i - 1, j] = (10 * (i - 1) + j).toDouble()
                    a[i, j - 1] = sin(1e-5 * a[i - 1, j])
                }
                a[i - 1, jSize - 1] = (10 * (i - 1) + jSize - 1).toDouble()

                MPI.COMM_WORLD.Allgather(a.a, i * (jSize) + startIndex2 - 1, endIndex2 - startIndex2 + 1, MPI.DOUBLE,
                        temp, 0, endIndex2 - startIndex2 + 1, MPI.DOUBLE)
//                    a.a, i * jSize, endIndex2 - startIndex2 + 1, MPI.DOUBLE)

                for (k in temp.indices) {
                    a[i, k] = temp[k]
                }

            }

//            val nano = measureNanoTime {

                for (i in startIndex..endIndex) {
                    for (j in 0..jSize - 1) {
                        result.append(a[i, j].toString())
                    }
                    result.append('\n')
                }
//            } / 1e6

//            println(nano)
//

            val charArray = result.toString().toCharArray()
            val charsSize = IntArray(procNumber)

            MPI.COMM_WORLD.Gather(intArrayOf(charArray.size), 0, 1, MPI.INT,
                    charsSize, 0, 1, MPI.INT, 0)

            recv = if (rank == 0) recv ?: CharArray(charsSize.sum()) else null
            if (recv?.size ?: Int.MAX_VALUE < charsSize.sum()) {
                recv = CharArray(charsSize.sum())
            }

            val intArray = IntArray(procNumber, { (0..(it - 1)).map { charsSize[it] }.sum() })


            MPI.COMM_WORLD.Gatherv(charArray, 0, charArray.count(), MPI.CHAR,
                    recv, 0, charsSize, intArray, MPI.CHAR, 0)

            if (rank == 0) {
                writer.write(recv)
            }


        }
        writer.close()

//        println(nanoTime2)

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
