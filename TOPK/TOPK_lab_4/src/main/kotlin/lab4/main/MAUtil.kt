package lab4.main

import lab4.util.codeMap
import lab4.util.errorMap
import lab4.util.simplePrecedence.Code
import lab4.util.simplePrecedence.Error

fun MA.translateStackLog(): String {
    return this.stackLog.map {
        it.map(::codeMap)
                .reduce { acc, s -> "$acc $s" }
    }.reduce { acc, s -> "$acc\n$s" }
}

fun MA.translateResult(): String {
    if (log == Code.SUCESS) return errorMap(log)

    val lastSize = stackLog.last().size

    if (lastSize > 3) {
        val lastElement: Int = stackLog.last().last()
        var preLastElement: Int

        preLastElement = stackLog.last()[lastSize - 2]
        if (preLastElement == Code.LEFT_OPERATION) {
            preLastElement = stackLog.last()[lastSize - 3]
        }

        return if (log == Error.RULE_NOT_FOUND)
            "${errorMap(log)}\n" +
                    "Программа не может заканчиваться символами: " +
                    codeMap(preLastElement) +
                    " и ${codeMap(lastElement)} "
        else
            "${errorMap(log)}\n" +
                    "Символы: " +
                    codeMap(preLastElement) +
                    " и ${codeMap(lastElement)} " +
                    "не могут стоять рядом"
    } else {
        return "${errorMap(log)}\n" +
                "Программа не может начинаться/заканчиваться ${codeMap(stackLog.last().last())}"
    }
}