package lab1.main

import lab1.main.FiniteStateAutomaton.Result

class RegexAdapter(regexString: String) : AutomatonInterface {
    private var matcher = getMatcherByRegex(regexString)

    fun changeRegex(regexString: String) {
        matcher = getMatcherByRegex(regexString)
    }

    private fun getMatcherByRegex(regexString: String) =
            regexString.toPattern().matcher("")


    override fun check(input: String?): FiniteStateAutomaton.Result =
            matcher.run {
                reset(input)

                when {
                    matches() -> Result.RIGHT
                    hitEnd() -> Result.ONGOING
                    else -> Result.WRONG
                }
            }
}