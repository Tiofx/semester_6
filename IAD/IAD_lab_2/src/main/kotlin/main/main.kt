package main

import util.CountryInformation
import util.getFromResources
import util.getShortCountryName
import util.parseCountrySet
import java.io.File

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


    europeInfo.intoFiles(packageName = "europe")
    americaInfo.intoFiles(packageName = "america")
    asiaInfo.intoFiles(packageName = "asia")
    africaInfo.intoFiles(packageName = "africa")
}

fun Set<CountryInformation>.intoFiles(basePath: String = "${System.getProperty("user.dir")}/out/outResources",
                                      packageName: String) {
    (0..3).forEach {
        File("$basePath/$packageName/${getColumnName(it)}.txt").printWriter().use {
            out ->
            out.println(this.getColumn(it)
                    .map(Any::toString)
                    .reduce { acc, s -> "$acc\n$s" }
                    .replace('.', ','))
        }
    }
}

fun getColumnName(number: Int) = when (number) {
    0 -> "name"
    1 -> "area"
    2 -> "population"
    3 -> "gdp"
    else -> "else"
}


fun Set<CountryInformation>.getColumn(number: Int) = this.map {
    when (number) {
        0 -> it.name
        1 -> it.area
        2 -> it.population
        3 -> it.gdp
        else -> throw java.security.InvalidParameterException()
    }
}.toList()


fun Set<CountryInformation>.getInfoAbout(partOfWorld: Set<String>) = this.filter {
    val countryName = it.name
    partOfWorld.any { it.getShortCountryName() == countryName }
}.toSet()

