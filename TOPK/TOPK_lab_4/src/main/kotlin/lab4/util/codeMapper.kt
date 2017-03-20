package lab4.util

import lab2.util.variantNative.Code.*
import lab4.util.simplePrecedence.Code
import lab4.util.simplePrecedence.Error

fun codeMap(code: Int) = when (code) {
    LexemeCode.STACK_PUSH -> "$"
    Code.LEFT_OPERATION -> "<•"
    LexemeCode.FRAGMENT -> "<фрагмент>"
    LexemeCode.PROGRAM_BEGIN -> "<начало программы>"
    LexemeCode.PROGRAM_BODY -> "<тело программы>"
    LexemeCode.OPERATOR -> "<оператор>"
    LexemeCode.OPERATOR_END -> "<конец оператора>"
    LexemeCode.OPERAND -> "<операнд>"
    LexemeCode.SIGN -> "<знак>"
    PROC -> "PROC"
    END -> "END"
    IDEN -> "<iden>"
    DATA -> "<data>"
    Sign.EQUALLY -> "="
    Sign.COMMA -> ","
    Sign.DOUBLE_AMPERSAND -> "&&"
    Sign.DOUBLE_EXCLAMATION_POINT -> "!!"
    Sign.LEFT_SHIFT -> "<<"
    Sign.RIGHT_SHIFT -> ">>"
    else -> "null"
}

fun errorMap(code: Int) = when (code) {
    Code.SUCESS -> "Успех. Ошибок нет"
    Error.PROGRAM_STRUCTURE_OUT -> "Ошибка в стукртуре программы"
    Error.PROGRAM_STRUCTURE_IN -> "Ошибка во внутренеей структуре"
    Error.OPERATOR_END -> "Ошибка в написании конца оператора"
    Error.OPERATOR -> "Ошибка в написании оператора"
    Error.SIGN_COMBINATION -> "Ошибка в комбинации знаков"
    Error.RULE_NOT_FOUND -> "Ошибка правило не найдено"
    else -> "неопознанная ошибка"
}