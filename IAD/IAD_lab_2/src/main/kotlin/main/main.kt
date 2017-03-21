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

    val union = union(area, population, gdp)

    println(union)
}

fun union(area: List<Area>, population: List<Population>, gdp: List<Gdp>): Set<CountryInformation> {
    val countrySet = area.map {
        val countryInformation = CountryInformation(it.country.getShortCountryName())
        countryInformation.area = it.area
        countryInformation
    }.toSet()

    population.forEach {
        val countryName = it.country
        countrySet.find { it.name == countryName.getShortCountryName() }?.population = it.populationAbsolute
    }

    gdp.forEach {
        val countryName = it.country
        countrySet.find { it.name == countryName.getShortCountryName() }?.gdp = it.gdp
    }

    val countryWithFullInfo = countrySet.filterNot {
        it.area == -1.0 || it.population == -1 || it.gdp == -1.0
    }.toSet()

    return countryWithFullInfo
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