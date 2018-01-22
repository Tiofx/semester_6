package lab2.util.variantNative

interface MessageAnalyzer {
    fun interpret(code: Int): String
}

object NullMessageAnalyzer : MessageAnalyzer {
    val message: String = "Серьезная ошибка: данный код отсутсвтует"

    override fun interpret(code: Int) = message
}

abstract class AbstractMessageAnalyzer(
        val code: Int,
        val message: String,
        var nextAnalyser: MessageAnalyzer
) : MessageAnalyzer {

    override fun interpret(code: Int) = if (canInterpret(code)) message else nextAnalyser.interpret(code)

    abstract protected fun canInterpret(inputCode: Int): Boolean

}

open class SpecificMessage(code: Int,
                           message: String,
                           nextAnalyser: MessageAnalyzer = NullMessageAnalyzer
) : AbstractMessageAnalyzer(code, message, nextAnalyser) {

    override protected fun canInterpret(inputCode: Int) = inputCode == code
}

open class TypeMessage(code: Int,
                       message: String,
                       nextAnalyser: MessageAnalyzer = NullMessageAnalyzer
) : AbstractMessageAnalyzer(code, message, nextAnalyser) {

    override fun interpret(code: Int) =
            "${message.takeIf { canInterpret(code) } ?: ""}${nextAnalyser.interpret(code)}"

    override protected fun canInterpret(inputCode: Int) =
            (inputCode % code < 100) and (inputCode - inputCode % code == code)
}


class KeyWordMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : TypeMessage(100, "< ключевое слово > : ", nextAnalyser)

class SignMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : TypeMessage(Code.SIGNS, "< знак > : ", nextAnalyser)

class ErrorMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : TypeMessage(Code.ERRORS, "Ошибка: ", nextAnalyser)


class ProcMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.PROC, "PROC", nextAnalyser)

class EndMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.END, "END", nextAnalyser)

class IdenMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.IDEN, "Переменная", nextAnalyser)

class DataMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.DATA, "Константа", nextAnalyser)


class EquallyMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.Sign.EQUALLY, "[ = ]", nextAnalyser)

class CommaMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.Sign.COMMA, "[ , ]", nextAnalyser)

class DoubleAmpersandMessage(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.Sign.DOUBLE_AMPERSAND, "[ && ]", nextAnalyser)

class L3Error(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.Error.L3, "данного символа нет в алфавите", nextAnalyser)

class InChainError(nextAnalyser: MessageAnalyzer = NullMessageAnalyzer)
    : SpecificMessage(Code.Error.IN_CHAIN, "некоректный фрагмент цепочки", nextAnalyser)















