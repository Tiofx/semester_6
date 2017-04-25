package task3

import golem.sin
import mpi.MPI
import java.io.PrintWriter
import java.lang.StringBuilder
import kotlin.system.measureNanoTime

//fun parallelTask3(args: Array<String>, iSize: Int, jSize: Int, iterationNumber: Int): MutableList<Long> {
//    val timeResult = mutableListOf<Long>()
//    val a = DoubleArray2D(iSize, jSize)
//
//    MPI.Init(args)
//
//    val comm = MPI.COMM_WORLD
//    val rank = comm.Rank()
//    val procNumber = comm.Size()
//
////    var recv: CharArray? = null
//    val temp = DoubleArray(jSize - 1)
//    val result = StringBuilder("")
//    val perProc = iSize / procNumber
//
//    val startIndex = rank * perProc
//    val endIndex = if (rank != procNumber - 1) startIndex + perProc - 1 else iSize - 1
//
//    val perProc2 = (jSize - 1) / procNumber
//    val startIndex2 = rank * perProc2 + 1
//    val endIndex2 = if (rank != procNumber - 1) startIndex2 + perProc2 - 1 else jSize - 1
//
//
//    var nanoTime2 = 0.0
//
//    repeat(iterationNumber) {
//        var writer = PrintWriter("file.txt")
//        writer.write("")
//        writer.close()
////        writer = PrintWriter("file.txt")
////        System.gc()
//        MPI.COMM_WORLD.Barrier()
//
//        val nanoTime = measureNanoTime {
//            for (i in startIndex..endIndex) {
//                for (j in 0..jSize - 1) {
//                    a[i, j] = (10 * i + j).toDouble()
//                }
//            }
//
//            MPI.COMM_WORLD.Allgather(a.a, startIndex * jSize, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE,
//                    a.a, 0, (endIndex - startIndex + 1) * jSize, MPI.DOUBLE)
//
//            for (i in 1..iSize - 1) {
//                for (j in startIndex2..endIndex2) {
//                    a[i, j - 1] = sin(1e-5 * a[i - 1, j])
//                }
//
//
//                MPI.COMM_WORLD.Allgather(a.a, i * (jSize) + startIndex2 - 1, endIndex2 - startIndex2 + 1, MPI.DOUBLE,
//                        temp, 0, endIndex2 - startIndex2 + 1, MPI.DOUBLE)
////                    a.a, i * jSize, endIndex2 - startIndex2 + 1, MPI.DOUBLE)
//
//                for (k in temp.indices) {
//                    a[i, k] = temp[k]
//                }
//
//            }
//
//            val nano = measureNanoTime {
//
//                for (i in startIndex..endIndex) {
//                    for (j in 0..jSize - 1) {
//                        result.append(a[i, j].toString())
//                    }
//                    result.append('\n')
//                }
//            } / 1e6
//
//            println(nano)
//
//            for (i in 0..procNumber - 1) {
//                if (rank == i) {
//                    writer = PrintWriter("file.txt")
//                    writer.append(result)
//                    writer.close()
//                }
//
//                MPI.COMM_WORLD.Barrier()
//            }
//
//
////            val charArray = result.toString().toCharArray()
////            val charsSize = IntArray(procNumber)
////
////            MPI.COMM_WORLD.Gather(intArrayOf(charArray.size), 0, 1, MPI.INT,
////                    charsSize, 0, 1, MPI.INT, 0)
////
////            recv = if (rank == 0) recv ?: CharArray(charsSize.sum()) else null
////            if (recv?.size ?: Int.MAX_VALUE < charsSize.sum()) {
////                recv = CharArray(charsSize.sum())
////            }
////
////            val intArray = IntArray(procNumber, { (0..(it - 1)).map { charsSize[it] }.sum() })
////
////
////            MPI.COMM_WORLD.Gatherv(charArray, 0, charArray.count(), MPI.CHAR,
////                    recv, 0, charsSize, intArray, MPI.CHAR, 0)
////            if (rank == 0) {
////                writer.write(recv)
////            }
//
//
//        }
//        writer.close()
//
////        println(nanoTime2)
//
//        timeResult.add(nanoTime)
//    }
//
//    MPI.Finalize()
//
//    return timeResult
//}
