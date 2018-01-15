import lab2.util.AutomationFactory
import lab2.util.variantNative.Code
import org.junit.Test
import kotlin.test.assertTrue

class TopkLab2Test {

    companion object {
        private val automation by lazy { AutomationFactory.createAutomation(21) }
    }

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


    private fun testOneWord(word: String, expectedCode: Int) = automation.run {
        sendLine(word)
        assertTrue("problem in $word") {
            log[0].automationPosition == expectedCode
        }
        reset()
    }
}