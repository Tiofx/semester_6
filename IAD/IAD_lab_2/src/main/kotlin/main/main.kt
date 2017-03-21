package main

import util.CountryInformation
import util.getFromResources
import util.getShortCountryName
import util.parseCountrySet

fun main(args: Array<String>) {
    val africa = "countries/africa.txt".getFromResources().parseCountrySet()

    val unionInformation = getUnionInformation()

    println(unionInformation.getInfoAbout(africa).size)
}


fun Set<CountryInformation>.getInfoAbout(partOfWorld: Set<String>) = this.filter {
    val countryName = it.name
    partOfWorld.any { it.getShortCountryName() == countryName }
}.toSet()

