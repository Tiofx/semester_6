package util

data class Area(val country: String, val area: Double)

data class Gdp(val country: String, val gdp: Double)

data class Population(val country: String, val populationAbsolute: Int, val populationRelative: Double)

data class CountryInformation(val name: String, var area: Double = -1.0, var population: Int = -1, var gdp: Double = -1.0)