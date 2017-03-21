package main

import util.CountryInformation
import util.getFromResources
import util.getShortCountryName
import util.parseCountrySet

fun main(args: Array<String>) {
    val europe = "countries/europe.txt".getFromResources().parseCountrySet()
    val america = "countries/america.txt".getFromResources().parseCountrySet()
    val asia = "countries/asia.txt".getFromResources().parseCountrySet()
    val africa = "countries/africa.txt".getFromResources().parseCountrySet()

    val unionInformation = getUnionInformation()

    val europeInfo = unionInformation.getInfoAbout(europe)
    val americaInfo = unionInformation.getInfoAbout(america)
    val asiaInfo = unionInformation.getInfoAbout(asia)
    val africaInfo = unionInformation.getInfoAbout(africa)
}

fun Set<CountryInformation>.getColumn(number: Int) = this.map {
    when (number) {
        0 -> it.name
        1 -> it.area
        2 -> it.population
        3 -> it.gdp
        else -> throw java.security.InvalidParameterException()
    }
}.toSet()


fun Set<CountryInformation>.getInfoAbout(partOfWorld: Set<String>) = this.filter {
    val countryName = it.name
    partOfWorld.any { it.getShortCountryName() == countryName }
}.toSet()

