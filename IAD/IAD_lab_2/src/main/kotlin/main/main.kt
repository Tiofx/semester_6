package main

import util.*
import java.io.File

fun main(args: Array<String>) {
    val africa = "countries/africa.txt".getFromResources().parseCountryList()
    val areaFile = "area.txt".getFromResources()
    val gdpFile = "gdp.txt".getFromResources()
    val populationFile = "population.txt".getFromResources()

    val area = areaFile.parseArea()
    val gdp = gdpFile.parseGdp()
    val population = populationFile.parsePopulation()

}

fun String.getShortCountryName() = this.substringBefore('(').trim()

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
        .map { Area(it[1], it[2].replace(" ", "").toDouble()) }
        .toList()

fun File.parseGdp() = this.readLines()
        .map(String::trim)
        .map { it.replace(",", ".") }
        .filter(String::isNotEmpty)
        .asSequence()
        .batch(2)
        .map {
            val list = it[0].split(Regex("[ \t\n]+"))
            Gdp(list[1], it[1].replace(" ", "").toDouble())
        }
        .toList()

fun File.parsePopulation() = this.readLines()
        .map(String::trim)
        .map { it.replace(",", ".") }
        .filter(String::isNotEmpty)
        .asSequence()
        .batch(4)
        .map {
            Population(
                    it[1],
                    it[2].replace(" ", "").toInt(),
                    it[3].replace("%", "").toDouble()
            )
        }
        .toList()