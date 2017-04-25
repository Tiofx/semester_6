package task1

import golem.plot
import golem.title
import golem.xlabel
import golem.ylabel
import task2.test.ParallelAndSequentialTime
import visualization.getNextColor

fun plotTask1Result(results: MutableList<Pair<Int, ParallelAndSequentialTime>>) {
    val x = results.map { it.first }.toIntArray()
    val yParallel = results.map { it.second.first }.toDoubleArray()
    val ySequential = results.map { it.second.second }.toDoubleArray()

    sequentialAndParallelPlot(x, yParallel, ySequential, 0)
    sequentialAndParallelComparisonPlot(x, results.map { it.second }, 1)
}

fun sequentialAndParallelPlot(x: IntArray, yParallel: DoubleArray, ySequential: DoubleArray, figure: Int) {
    golem.figure(figure)
    plot(x, yParallel, getNextColor(0), "параллельный алгоритм")
    plot(x, ySequential, getNextColor(0), "последовательный алгоритм")

    ylabel("Время, мс")
    xlabel("Количество эллементов")
    title("Задание 1")
}


fun sequentialAndParallelComparisonPlot(x: IntArray, y: List<ParallelAndSequentialTime>, figure: Int) {
    golem.figure(figure)
    plot(x, y.map { (it.second - it.first) / it.first }.map { 100 * it }.toDoubleArray(),
            getNextColor(figure), "разница в процентах")

    ylabel("Проценты, %")
    xlabel("Количество эллементов")
    title("Зависимость эффективности параллельного алгоритма от количества элементов")
}