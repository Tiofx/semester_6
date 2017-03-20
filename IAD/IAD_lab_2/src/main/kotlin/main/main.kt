package main

import util.getFromResources
import java.io.File

fun main(args: Array<String>) {
    val set = "countries/africa.txt".getFromResources().parseCountryList()

    println(set)
}

fun File.parseCountryList() = this.readLines()
        .map { it.replace(Regex("\\d+."), "") }
        .map(String::trim)
        .filter(String::isNotEmpty)
        .toSet()

fun File.parseArea() = 1

fun File.parseGdp() = 1

fun File.parsePopulation() = 1