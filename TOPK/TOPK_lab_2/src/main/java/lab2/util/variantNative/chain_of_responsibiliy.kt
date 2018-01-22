package lab2.util.variantNative

interface Handler {
    fun handle(code: Int): String
}

object NullHandler : Handler {
    val message: String = "Серьезная ошибка: данный код отсутсвтует"

    override fun handle(code: Int) = message
}

abstract class AbstractHandler(
        val code: Int,
        val message: String,
        var handler: Handler
) : Handler {

    override fun handle(code: Int) = if (canHandle(code)) message else handler.handle(code)

    abstract protected fun canHandle(inputCode: Int): Boolean

}

open class SpecificMessage(code: Int,
                           message: String,
                           handler: Handler = NullHandler
) : AbstractHandler(code, message, handler) {

    override protected fun canHandle(inputCode: Int) = inputCode == code
}

open class TypeMessage(code: Int,
                       message: String,
                       handler: Handler = NullHandler
) : AbstractHandler(code, message, handler) {

    override fun handle(code: Int) =
            "${message.takeIf { canHandle(code) } ?: ""}${handler.handle(code)}"

    override protected fun canHandle(inputCode: Int) =
            (inputCode % code < 100) and (inputCode - inputCode % code == code)
}


class KeyWordMessage(handler: Handler = NullHandler) : TypeMessage(100, "< ключевое слово > : ", handler)
class SignMessage(handler: Handler = NullHandler) : TypeMessage(Code.SIGNS, "< знак > : ", handler)
class ErrorMessage(handler: Handler = NullHandler) : TypeMessage(Code.ERRORS, "Ошибка: ", handler)

class ProcMessage(handler: Handler = NullHandler) : SpecificMessage(Code.PROC, "PROC", handler)
class EndMessage(handler: Handler = NullHandler) : SpecificMessage(Code.END, "END", handler)
class IdenMessage(handler: Handler = NullHandler) : SpecificMessage(Code.IDEN, "Переменная", handler)
class DataMessage(handler: Handler = NullHandler) : SpecificMessage(Code.DATA, "Константа", handler)

class EquallyMessage(handler: Handler = NullHandler) : SpecificMessage(Code.Sign.EQUALLY, "[ = ]", handler)
class CommaMessage(handler: Handler = NullHandler) : SpecificMessage(Code.Sign.COMMA, "[ , ]", handler)
class DoubleAmpersandMessage(handler: Handler = NullHandler) : SpecificMessage(Code.Sign.DOUBLE_AMPERSAND, "[ && ]", handler)

class L3Error(handler: Handler = NullHandler) : SpecificMessage(Code.Error.L3, "данного символа нет в алфавите", handler)
class InChainError(handler: Handler = NullHandler) : SpecificMessage(Code.Error.IN_CHAIN, "некоректный фрагмент цепочки", handler)















