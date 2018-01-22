import lab1.main.Adapter
import lab1.main.FiniteStateAutomaton
import lab1.main.FiniteStateAutomatonInterface
import org.junit.Test
import kotlin.test.assertSame


infix fun String.matches(automaton: FiniteStateAutomatonInterface) = automaton isValid this

infix fun FiniteStateAutomatonInterface.isValid(string: String) =
        check(string) == FiniteStateAutomaton.Result.RIGHT


interface ITopkLab1Test {
    fun assertSameByResult(test: String, expected: FiniteStateAutomaton.Result)
    fun assertSameByInput(test: String)

}

abstract class AbstractTopkLab1Test : ITopkLab1Test {
    @Test
    fun allowableInput1() {
        listOf("c", "ccccc", "abc", "abcccc", "ababc", "ababccc", "ababababcccccc")
                .forEach(::assertSameByInput)
    }

    @Test
    fun allowableInput2() {
        listOf("cab", "cabbbbbbb", "cabc", "caba", "cabaaccaacacacacccc", "cabbbbcccaaacacac")
                .forEach(::assertSameByInput)
    }

    @Test
    fun unallowableInput1() {
        listOf("", " ", "a", "aba", "ac", "abcab", "123", "a233sdfcxlvkjbklsdjfad")
                .forEach(::assertSameByInput)
    }

    @Test
    fun unallowableInput2() {
        listOf("ca", "caaac", "cabcb", "cabab", "cabcccaaab", "cab123")
                .forEach(::assertSameByInput)
    }

    @Test
    fun resultOngoing1() {
        listOf("", "a", "ab", "aba", "ababa")
                .forEach { assertSameByResult(it, FiniteStateAutomaton.Result.ONGOING) }
    }

    @Test
    fun resultOngoing2() {
        listOf("ca")
                .forEach { assertSameByResult(it, FiniteStateAutomaton.Result.ONGOING) }
    }

    @Test
    fun resultWrong() {
        listOf("aa", "abb", "ac", "ab123", "123", "car", "caa", "cab1", "c1", "cabbbcacac3")
                .forEach { assertSameByResult(it, FiniteStateAutomaton.Result.WRONG) }
    }
}

open class TopkLab1TestForRefactoring : AbstractTopkLab1Test() {
    companion object {
        private val pattern by lazy {
            ("(ab)*c+" to "cab+[ca]*").run { "^($first|$second)$" }.toRegex()
        }
        private val automation by lazy { FiniteStateAutomaton() }
    }

    override fun assertSameByResult(test: String, expected: FiniteStateAutomaton.Result) {
        assertSame(expected, automation.check(test), "wrong test: [$test]")
    }

    override fun assertSameByInput(test: String) {
        assertSame(test matches pattern, test matches automation, test)
    }

}
