package task3

import golem.*
import task2.test.ParallelAndSequentialTime
import visualization.getNextColor

fun plotTask3Result(results: MutableList<Triple<Int, Int, ParallelAndSequentialTime>>) {
    val x = results.map { it.first * it.second }.toIntArray()
    val yParallel = results.map { it.third.first }.toDoubleArray()
    val ySequential = results.map { it.third.second }.toDoubleArray()

    sequentialAndParallelPlot(x, yParallel, ySequential, 0)
    sequentialAndParallelComparisonPlot(x, results.map { it.third }, 1)
}

fun sequentialAndParallelPlot(x: IntArray, yParallel: DoubleArray, ySequential: DoubleArray, figure: Int) {
    figure(figure)
    plot(x, yParallel, getNextColor(0), "параллельный алгоритм")
    plot(x, ySequential, getNextColor(0), "последовательный алгоритм")

    ylabel("Время, мс")
    xlabel("Суммарное количество эллементов")
    title("Задание 3")
}


fun sequentialAndParallelComparisonPlot(x: IntArray, y: List<ParallelAndSequentialTime>, figure: Int) {
    figure(figure)
    plot(x, y.map { (it.second - it.first) / it.first }.map { 100 * it }.toDoubleArray(),
            getNextColor(figure), "разница в процентах")

    ylabel("Проценты, %")
    xlabel("Суммарное количество эллементов")
    title("Зависимость эффективности параллельного алгоритма от количества элементов")
}