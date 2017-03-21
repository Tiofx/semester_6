package util

import java.io.File

fun String.getFromResources(): File = File(Thread.currentThread().contextClassLoader.getResource(this).file)

fun String.getShortCountryName() = this.substringBefore('(').trim()

fun File.parseCountrySet() = this.readLines()
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