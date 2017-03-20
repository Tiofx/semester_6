import golem.mapRowsToList
import golem.matrix.Matrix
import golem.set
import golem.zeros
import lab4.util.simplePrecedence.Code
import java.io.File

fun String.getFileFromResources() = File(Thread.currentThread().contextClassLoader.getResource(this).file)

fun adjacencyListToMatrix(file: File): Matrix<Double> {
    val listList = file.readLines()
            .map { it ->
                it.split('\t', '\n', ' ')
                        .filter { it != "" }
                        .map(String::toInt)
                        .toList()
            }.filter { it -> !it.isEmpty() }
            .toList()

    val resultMatrix = zeros(listList[0][0], listList[0][0])

    listList.stream()
            .skip(1)
            .forEach { resultMatrix[it[0], it[1]] = it[2] }

    return resultMatrix
}

fun File.toList(): List<Int> {
    return this
            .readLines()
            .map { it ->
                it.split('\t', '\n', ' ')
                        .filter { it != "" }
                        .map(String::toInt)
                        .toList()
            }.filter { it -> !it.isEmpty() }
            .reduce { acc, list -> acc + list }
}

fun File.toArrayIntArray(): Array<IntArray> {
    return this
            .readLines()
            .map { it ->
                it.split('\t', '\n', ' ')
                        .filter { it != "" }
                        .map(String::toInt)
                        .toIntArray()
            }.filter { it -> !it.isEmpty() }
            .toTypedArray()
}

fun Matrix<Double>.toPrecedenceString(): String {
    return this
            .mapRowsToList(Matrix<Double>::toList)
            .map {
                it.toList()
                        .map {
                            when (it.toInt()) {
                                Code.LEFT_OPERATION -> "< |"
                                Code.RIGHT_OPERATION -> " >|"
                                Code.EQUALL -> " ≡|"
                                else -> " -|"
                            }
                        }.reduce { acc, s -> acc + s }
            }.reduce { acc, s -> acc + "\n" + s }
}

fun Matrix<Double>.toIntMatrixString(): String {
    return this
            .mapRowsToList(Matrix<Double>::toList)
            .map {
                it.toList()
                        .map { it.toInt().toString() }
                        .reduce { acc, s -> "$acc $s" }
            }.reduce { acc, s -> acc + "\n" + s }
}
