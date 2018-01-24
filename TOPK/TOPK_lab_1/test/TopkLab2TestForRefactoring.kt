import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import lab2.util.*
import lab2.util.variantNative.*
import org.junit.Test
import kotlin.test.assertTrue

abstract class AbstractTopkLab2Test {
    @Test
    fun testProc() {
        listOf("proc", "Proc", "PROC", "PrOc", "proc ", "  PRoc")
                .forEach { testOneWord(it, Code.PROC) }
    }

    @Test
    fun testEnd() {
        listOf("end", "End", "END", " END", " end  ")
                .forEach { testOneWord(it, Code.END) }
    }

    @Test
    fun testIden() {
        listOf("proc0100", "adsflkj0100asdlfkj0", "abc010000")
                .forEach { testOneWord(it, Code.IDEN) }
    }

    @Test
    fun testConstant() {
        listOf("3.3E+11", "12.34E-99", "1.23456E-00", "12345.6E+67")
                .forEach { testOneWord(it, Code.DATA) }
    }

    @Test
    fun testSign() {
        listOf(
                "=" to Code.Sign.EQUALLY,
                "," to Code.Sign.COMMA,
                "&&" to Code.Sign.DOUBLE_AMPERSAND,
                "!!" to Code.Sign.DOUBLE_EXCLAMATION_POINT,
                "<<" to Code.Sign.LEFT_SHIFT,
                ">>" to Code.Sign.RIGHT_SHIFT
        ).forEach { testOneWord(it.first, it.second) }
    }

    @Test
    fun testError1() {
        listOf("p", "pr", "pro", "e", "en")
                .forEach { testOneWord(it, Code.Error.IN_KEY_WORD) }
    }

    @Test
    fun testError2() {
        listOf("a", "abc", "ab0100ab")
                .forEach { testOneWord(it, Code.Error.IN_VARIABLE) }
    }

    @Test
    fun testError3() {
        listOf("12.34E-991", "1.23456E-1", "12345.6789E+67")
                .forEach { testOneWord(it, Code.Error.IN_CONSTANT) }
    }

    abstract protected fun testOneWord(word: String, expectedCode: Int)
}

class TopkLab2TestForRefactoring : AbstractTopkLab2Test() {

    companion object {
        private val automation by lazy { AutomationFactory.createAutomation(21) }
    }

    protected override fun testOneWord(word: String, expectedCode: Int) = automation.run {
        sendLine(word)
        assertTrue("problem in $word") {
            log[0] equalTo LogInfo(1, word.trimEnd().length + 1, expectedCode)
        }
        reset()
    }
}


class TestLab2ChainOfResponsibility {

    private val messageAnalyzer: MessageAnalyzer by lazy {
        listOf(KeyWordMessage(), SignMessage(), ErrorMessage(),
                ProcMessage(), EndMessage(), IdenMessage(), DataMessage(),
                EquallyMessage(), CommaMessage(), DoubleAmpersandMessage(),
                L3Error(), InChainError()
        )
                .apply { bindEachToNextHandler() }
                .first()
    }

    private val codeAnalyzer by lazy { CodesAnalyser() }

    @Test
    fun test() {
        listOf(Code.PROC, Code.END, Code.IDEN, Code.DATA,
                Code.Sign.EQUALLY, Code.Sign.COMMA, Code.Sign.DOUBLE_AMPERSAND,
                Code.Error.L3, Code.Error.IN_CHAIN, 1233212)
                .forEach {
                    assertTrue("$it : ${codeAnalyzer.interpret(it)} == ${messageAnalyzer.interpret(it)}") {
                        codeAnalyzer.interpret(it) == messageAnalyzer.interpret(it)
                    }
                }
    }

}

class TestLab2AbstractFactory : AbstractTopkLab2Test() {
    private val factory: AbstractAutomationFactory by lazy { AutomationFactoryLab2() }

    override fun testOneWord(word: String, expectedCode: Int) {
        factory.createAutomation().run {
            sendLine(word)
            assertTrue("problem in $word") {
                log[0] equalTo LogInfo(1, word.trimEnd().length + 1, expectedCode)
            }
            reset()
        }
    }
}

private fun List<AbstractMessageAnalyzer>.bindEachToNextHandler() {
    generateSequence(0) { it + 1 }
            .map { it to it + 1 }
            .takeWhile { it.first <= lastIndex }
            .map { get(it.first) to getOrNull(it.second) }
            .forEach {
                it.first.nextAnalyser = it.second ?: NullMessageAnalyzer
            }
}

infix fun LogInfo.notEqualTo(logInfo: LogInfo) = !(this equalTo logInfo)
infix fun LogInfo.equalTo(logInfo: LogInfo) = logInfo.let {
    (row == it.row) and (column == it.column) and (automationPosition == it.automationPosition)
}