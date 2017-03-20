package lab4.preCalculations

import getFileFromResources

fun main(args: Array<String>) {
    val file = "table_lab_4_v6.txt".getFileFromResources()
//    val file = "test_primer.txt".getFileFromResources()
//    val file = "test_srs.txt".getFileFromResources()

    println(calculateMatrix(file))
}