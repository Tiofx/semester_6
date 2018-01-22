package lab1

fun regexForLab1() = ("(ab)*c+" to "cab+[ca]*").run { "^($first|$second)$" }