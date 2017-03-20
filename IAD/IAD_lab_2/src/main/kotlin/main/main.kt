package main

import util.area
import util.batch
import util.getFromResources
import java.io.File

fun main(args: Array<String>) {
    val set = "countries/africa.txt".getFromResources().parseCountryList()
    val areaFile = "area.txt".getFromResources()

    println(set)
    println(areaFile.parseArea())
}

fun File.parseCountryList() = this.readLines()
        .map { it.replace(Regex("\\d+."), "") }
        .map(String::trim)
        .filter(String::isNotEmpty)
        .toSet()

fun File.parseArea() = this.readLines()
        .map(String::trim)
        .map { it.replace(",", ".") }
        .filter(String::isNotEmpty)
        .asSequence()
        .batch(3)
        .map { area(it[0].toInt(), it[1], it[2].replace(" ", "").toDouble()) }
        .toList()

fun File.parseGdp() = 1

fun File.parsePopulation() = 1