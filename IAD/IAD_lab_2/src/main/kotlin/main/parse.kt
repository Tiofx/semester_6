package main

import util.*
import java.io.File

fun getUnionInformation(areaFile: File = "area.txt".getFromResources(),
                        populationFile: File = "population.txt".getFromResources(),
                        gdpFile: File = "gdp.txt".getFromResources()): Set<CountryInformation> =
        union(areaFile.parseArea(), populationFile.parsePopulation(), gdpFile.parseGdp())

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