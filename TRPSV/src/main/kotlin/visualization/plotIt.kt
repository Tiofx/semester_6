package visualization

import golem.*
import task2.test.ResultSet

fun plot(makeTest: ResultSet) {
    val x = makeTest.results.map { it.input.vertexNumber }.toIntArray()
    val y1 = makeTest.results.map { it.millisecondTime.first }.toDoubleArray()
    val y2 = makeTest.results.map { it.millisecondTime.second }.toDoubleArray()

    figure(1)
    golem.plot(x, y1, lineLabel = "Параллельный алгоритм")
    golem.plot(x, y2, "b", lineLabel = "Последовательный алгоритм")
    ylabel("Время, мс")
    xlabel("Количество вершин")
}


fun allEdgeProbabilityPlot(makeTest: ResultSet) {
    val x = makeTest.results.map { it.input.vertexNumber }.distinct().toIntArray()
    val groupByEdge = makeTest.results.groupBy { it.input.edgeProbability }

    groupByEdge.forEach { edgeProbability, u ->
        allEdgeProbabilityPlot(x, u.map { it.millisecondTime.second }.toDoubleArray(), edgeProbability, 0)
        allEdgeProbabilityPlot(x, u.map { it.millisecondTime.first }.toDoubleArray(), edgeProbability, 1, true)
    }
}

fun allEdgeProbabilityPlot(x: IntArray, y: DoubleArray, edgeProbability: Double, figure: Int, isParallel: Boolean = false) {
    figure(figure)
    plot(x, y, getNextColor(figure), "разяженность графа: ${edgeProbability}")

    ylabel("Время, мс")
    xlabel("Количество вершин")
    title("${if (isParallel) "Параллельный" else "Последовательный"} алгоритм")
}


val figureColors = mutableMapOf(0 to plotColors.iterator())

fun getNextColor(figure: Int): String {
    if (!figureColors.contains(figure) || !figureColors[figure]!!.hasNext()) {
        figureColors.put(figure, plotColors.iterator())
    }

    return figureColors[figure]!!.next().key
}