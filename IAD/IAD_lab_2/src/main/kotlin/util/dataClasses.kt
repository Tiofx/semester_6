package util

data class Area(val country: String, val area: Double)

data class Gdp(val country: String, val gdp: Double)

data class Population(val country: String, val populationAbsolute: Int, val populationRelative: Double)

data class CountryInformation(val name: String, val area: Double, val population: Int, val gdp: Double)