import lab1.main.FiniteStateAutomaton
import org.junit.Test
import kotlin.test.assertSame

class TopkLab1Test1 {

    companion object {
        private val pattern by lazy {
            ("(ab)*c+" to "cab+[ca]*").run { "^($first|$second)$" }.toRegex()
        }
        private val automation by lazy { FiniteStateAutomaton() }
    }

    @Test
    fun allowableInput1() {
        listOf("c", "ccccc", "abc", "abcccc", "ababc", "ababccc", "ababababcccccc")
                .forEach(::assertSame)
    }

    @Test
    fun allowableInput2() {
        listOf("cab", "cabbbbbbb", "cabc", "caba", "cabaaccaacacacacccc", "cabbbbcccaaacacac")
                .forEach(::assertSame)
    }

    @Test
    fun unallowableInput1() {
        listOf("", " ", "a", "aba", "ac", "abcab", "123", "a233sdfcxlvkjbklsdjfad")
                .forEach(::assertSame)
    }

    @Test
    fun unallowableInput2() {
        listOf("ca", "caaac", "cabcb", "cabab", "cabcccaaab", "cab123")
                .forEach(::assertSame)
    }


    private fun assertSame(test: String) {
        assertSame(test matches pattern, test matches automation, test)
    }
}

infix fun String.matches(automaton: FiniteStateAutomaton) = automaton isValid this

infix fun FiniteStateAutomaton.isValid(string: String) =
        isValid(string) == FiniteStateAutomaton.Result.RIGHT