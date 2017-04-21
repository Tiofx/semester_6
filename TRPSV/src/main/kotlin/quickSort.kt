package main.kotlin

fun IntArray.quickSort(from: Int = 0, to: Int = this.size - 1) {
    if (from < to) {
        val q = partition(this, from, to)
        this.quickSort(from, q - 1)
        this.quickSort(q + 1, to)
    }
}

private fun partition(array: IntArray, left: Int, right: Int): Int {
    val pivot = array[right]
    var i = left - 1

    for (j in left..right - 1) {
        if (array[j] <= pivot) {
            i++
            array.swap(i, j)
        }
    }

    array.swap(i + 1, right)

    return i + 1
}

fun IntArray.swap(i: Int, j: Int) {
    val t = this[i]
    this[i] = this[j]
    this[j] = t
}