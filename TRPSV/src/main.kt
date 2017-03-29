import mpi.MPI
import java.io.File
import java.util.*
import kotlin.system.measureTimeMillis

//
val elementNumber = 32 * 1e3.toInt()
val iterationNumber = 100


//val elementNumber = 32
//val iterationNumber = 1
//
fun main(args: Array<String>) {

    val parallelTime = work(args)
//
//    MPI.Init(args)
//    val comm = MPI.COMM_WORLD
//
    if (MPI.COMM_WORLD.Rank() == 0) {
        println(parallelTime.map(Long::toString).reduce { acc, s -> "$acc\n$s" })

        println("""
            |-------------------------------------------------------------
            | количество элементов: $elementNumber
            | количество итераций: $iterationNumber
            | затраченное время на последовательную реализацию: ${sort(elementNumber, iterationNumber)} мс
            | затраченное время на параллельную реализацию:     ${parallelTime.average()} мс
            |-------------------------------------------------------------
    """.trimMargin())

    }
//    MPI.Finalize()
}

private fun sort(size: Int, iterationNumber: Int): Double {
    val maxValue = size / 10
    var array = generateArray(size, maxValue)
    var totalSortTime = 0L

    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")
    println("=========================")

    for (i in 0..iterationNumber - 1) {
        val measureTimeMillis = measureTimeMillis { array.quickSort() }
        totalSortTime += measureTimeMillis
//        println(measureTimeMillis)
//        shuffle(array)
        array = generateArray(size, maxValue)
    }

    println("====")

    return totalSortTime / iterationNumber.toDouble()
}

const val filename: String = "array.txt"

fun generateArray(size: Int, maxValue: Int): IntArray {
    val numbers = IntArray(size)
    val list = File(filename).readLines()
            .flatMap { it.split(Regex("[ \t\n]")) }
            .filter(String::isNotBlank)
            .map(String::toInt)

    if (list.isEmpty() || list[0] != size) {
        val generator = Random()

        for (i in numbers.indices) {
            numbers[i] = generator.nextInt(maxValue)
        }

        File(filename).writeText(
                "$size\n" +
                        numbers.map(Any::toString)
                                .reduceRight { s, acc -> "$acc $s" }
        )

        return numbers
    } else {
        (0..size - 1).forEach { numbers[it] = list[it + 1] }

        return numbers
    }

}

fun shuffle(array: IntArray) {
    val n = array.size
    for (i in array.indices) {
        val random = i + (Math.random() * (n - i)).toInt()

        val randomElement = array[random]
        array[random] = array[i]
        array[i] = randomElement
    }
}